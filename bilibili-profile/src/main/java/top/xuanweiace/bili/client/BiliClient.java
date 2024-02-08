package top.xuanweiace.bili.client;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import top.xuanweiace.bili.Utils.BilibiliCookieUtils;
import top.xuanweiace.bili.Utils.MUtils;
import top.xuanweiace.bili.conf.BiliConf;

import java.io.IOException;
import java.io.PrintStream;
import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * @author zxz
 * @date 2023/5/11 17:34
 */
@Component
@Slf4j
public class BiliClient {
    @Autowired
    BiliConf biliConf;
    @Autowired
    RestTemplate restTemplate;

    public List<HttpCookie> cookies;

    private final String GET_HISTORY_PATTERN = "https://api.bilibili.com/x/v2/history?pn=%d&ps=%d";
    private final String GET_MY_DYNAMIC_URL = "https://api.bilibili.com/x/polymer/web-dynamic/v1/feed/all";

    //刷新Cookie相关url
    private final String refresh_csrf_url_PATTERN = "https://www.bilibili.com/correspond/1/%s";
    private final String refresh_cookie_url = "https://passport.bilibili.com/x/passport-login/web/cookie/refresh";

    public VideoHistoryResp getHistory(int pageSize) {
        return getHistory(1, pageSize);
    }

    //控制返回值必须非null
    @NotNull
    public VideoHistoryResp getHistory(int pageNum, int pageSize) {

        // API 限制，在配置里，目前上限300
        if (pageSize > biliConf.maximumNumberOfVideosFetchedOnce) {
            pageSize = biliConf.maximumNumberOfVideosFetchedOnce;
        }
        String url = String.format(GET_HISTORY_PATTERN, pageNum, pageSize);
        log.info("url={}", url);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add(getBilibiliClientCookie());
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        log.info("resp={}", MUtils.logCut(resp.toString()));
        VideoHistoryResp result = JSON.parseObject(resp.getBody(), VideoHistoryResp.class);
        return result;
        //最好别下面这么写吧。因为不然的话，VideoHistoryResp里的List你也要这么处理，甚至里面所有对象都需要这样处理。
//        return result != null ? result : new VideoHistoryResp();
    }

    @NotNull
    public MyDynamicBILIResp getMyDynamic() {
        log.info("url={}", GET_MY_DYNAMIC_URL);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add(getBilibiliClientCookie());
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(GET_MY_DYNAMIC_URL, HttpMethod.GET, entity, String.class);

        log.info("resp={}", MUtils.logCut(resp.toString()));
        MyDynamicBILIResp result = JSON.parseObject(resp.getBody(), MyDynamicBILIResp.class);
        return result;
    }

    // 刷新Cookie的起点函数
    //TODO 后续所有都用Exception而不是boolean
    public boolean refreshCookie() {
        String correspondPath = "";
        try {
            correspondPath = BilibiliCookieUtils.getCorrespondPath(System.currentTimeMillis());
        } catch (Exception e) {
            log.error("[BiliClient.refreshCookie]刷新失败，原因是{}", e);
            return false;
        }
        // 1. 先获取refresh_csrf

        String url = String.format(refresh_csrf_url_PATTERN, correspondPath);
        System.out.println("url="+url);
        //方法1
//        OkHttpClient client = new OkHttpClient().newBuilder()
//                .build();
//        Request request = new Request.Builder()
//                .url(url)
//                .addHeader("Cookie", getBilibiliClientCookie())
//                .addHeader("User-Agent", "Apifox/1.0.0 (https://apifox.com)")
//                .build();
//        String respBody = "";
//        try {
//            Response response = client.newCall(request).execute();
//            respBody = response.body().string();
////            System.out.println(respBody);
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }

        //方法2
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add(getBilibiliClientCookie());
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.set(HttpHeaders.USER_AGENT,"Apifox/1.0.0 (https://apifox.com)");
        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);


        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);
        String respBody = null;
        try {
            respBody = new String(resp.getBody().getBytes("ISO-8859-1"), "UTF-8");
        } catch (UnsupportedEncodingException e) {
            throw new RuntimeException(e);
        }
        System.out.println("respBody: " + respBody);

        Pattern pattern = Pattern.compile("<div id=\"1-name\">(.*?)</div>");
        Matcher matcher = pattern.matcher(respBody);
        String refresh_csrf = "";
        if (matcher.find()) {
            refresh_csrf = matcher.group(1);
            log.info("已经获取到refresh_csrf={}", refresh_csrf);
        } else {
            log.error("获取refresh_csrf失败,url={},resp={}",url, respBody);
            return false;
        }

        // 2. 刷新Cookie
        MultiValueMap<String, String> body = new LinkedMultiValueMap<>();
        System.out.println("csrf = " + getCookieValueByKey("bili_jct"));
        System.out.println("refresh_csrf = " + refresh_csrf);
        System.out.println("refresh_token = " + biliConf.getRefreshToken());
        body.add("csrf", getCookieValueByKey("bili_jct"));
        body.add("refresh_csrf", refresh_csrf);
        body.add("source", "main_web");
        body.add("refresh_token", biliConf.getRefreshToken());

        HttpEntity<MultiValueMap<String, String>> requestEntity = new HttpEntity<>(body, headers);

        ResponseEntity<Map<String, Object>> response = restTemplate.exchange(
                refresh_cookie_url,
                HttpMethod.POST,
                requestEntity,
                new ParameterizedTypeReference<Map<String, Object>>() {}
        );
        Map<String, Object> b = response.getBody();
        if((Integer)b.get("code") != 0) {
            log.error("刷新Cookie步骤时出错,url={},resp={}",refresh_cookie_url,response.toString());
            return false;
        }
        List<String> setCookies = response.getHeaders().get(HttpHeaders.SET_COOKIE);
        System.out.println("setCookies strings = " + setCookies);
        setToBilibiliClientCookie(setCookies);
        Map<String, Object> data = (Map<String, Object>) b.get("data");
        String refresh_token = (String) data.get("refresh_token");
        biliConf.setRefreshToken(refresh_token);
        return true;
    }

    private List<HttpCookie> convertStringToCookies(String cookiesString) {
        List<HttpCookie> httpCookies = new ArrayList<>();
        String[] cookiesArray = cookiesString.split(";");
        for (String cookieString : cookiesArray) {
            String[] cookiePair = cookieString.trim().split("=");
            if (cookiePair.length == 2) {
                HttpCookie cookie = new HttpCookie(cookiePair[0], cookiePair[1]); // 使用cookie对象进行后续操作
                httpCookies.add(cookie);
            } else {
                log.error("这cookie不太对！！请核查！！！cookieString={},cookiePair={}", cookieString,cookiePair);
            }
        }
        return httpCookies;
    }

    private String getCookieValueByKey(String key) {
        for (HttpCookie cookie : cookies) {
            if (cookie.getName().equals(key)) {
                return cookie.getValue();
            }
        }
        return "";
    }

    private String convertCookieToString(List<HttpCookie> cookies) {
        StringBuilder cookieStringBuilder = new StringBuilder();
        for (HttpCookie cookie : cookies) {
            cookieStringBuilder.append(cookie.getName()).append("=").append(cookie.getValue()).append(";");
        }
        //删除最后的分号
        if (cookieStringBuilder.length() > 0) {
            cookieStringBuilder.deleteCharAt(cookieStringBuilder.length() - 1);
        }
        return cookieStringBuilder.toString();
    }

    //所有要查询cookie的都走这里，TODO 后续抽象为CookieManager
    private String getBilibiliClientCookie() {
        if (cookies != null && cookies.size() > 0) {
            return convertCookieToString(this.cookies);
        }
        cookies = convertStringToCookies(biliConf.userCookie);
        return biliConf.userCookie;// 兜底
    }
    private boolean setToBilibiliClientCookie(List<String> setCookies) {
        Map<String,String> mp = new HashMap<>();
        List<HttpCookie> newCookies = new ArrayList<>();
        for(String c : setCookies) {
            List<HttpCookie> httpCookies = convertStringToCookies(c);
            for(HttpCookie httpCookie :httpCookies) {
                if(mp.containsKey(httpCookie.getName())) {
                    if(mp.get(httpCookie.getName()).equals(httpCookie.getValue()) == false) {
                        log.error("[setToBilibiliClientCookie]有问题请排查，多个Set-Cookie中，相同key但是value不同,已有为({},{}),新的为{}",httpCookie.getName(),mp.get(httpCookie.getName()),httpCookie);
                    }
                } else {
                    newCookies.add(httpCookie);
                    mp.put(httpCookie.getName(),httpCookie.getValue());
                }
            }
        }
        cookies = newCookies;
        return true;
    }

}


//import org.springframework.http.*;
//import org.springframework.util.LinkedMultiValueMap;
//import org.springframework.util.MultiValueMap;
//import org.springframework.web.client.RestTemplate;
//
//import java.util.Map;
//
//public class Main {
//    publicstaticvoidmain(String[] args) {
//        RestTemplaterestTemplate = newRestTemplate();
//        HttpHeadersheaders = newHttpHeaders();
//        headers.set("Cookie", "SESSDATA=c0ff6525%2C1722873629%2C461e8%2A21CjB-IJgvNmvt1Z0aeCc_XCoFPZg4eOi81ICvzgsVaGX1MkLiiTIAWwEs1mpyTs7sxzASVkg5dUNva3ZvLXphaXc4dTdXMTVyWGhndUxrXzdMVTFuQ2ZOUU9OX1p5Nk5PQW5EY1htMUdBSjNYSzc2RFpMT3hRRU1ZaXRyR1YyTjZuU1FZbGVKNzlBIIEC");
//        headers.set("User-Agent", "Apifox/1.0.0(https://apifox.com)");
//        MultiValueMap<String, String> body = newLinkedMultiValueMap <>();
//        body.add("csrf", "d93c3ab19225fd6ddab299a7cc977c58");
//        body.add("refresh_csrf", "8999626fb81c52a0dbfd218b89babbf6");
//        body.add("source", "main_web");
//        body.add("refresh_token", "9c8dbf22315faefb038102cc5539d721");
//        HttpEntity<MultiValueMap<String, String>> requestEntity = newHttpEntity <>(body, headers);
//        ResponseEntity<Map<String, Object>> response = restTemplate.exchange("https://passport.bilibili.com/x/passport-login/web/cookie/refresh", HttpMethod.POST, requestEntity, new ParameterizedTypeReference < Map < String, Object >> ()
//        {
//        });
//        Map<String, Object> responseBody = response.getBody();
//        System.out.println(responseBody.get("code"));
//        System.out.println(responseBody.get("message"));
//        System.out.println(responseBody.get("ttl"));
//        System.out.println(responseBody.get("data"));
//    }
//}