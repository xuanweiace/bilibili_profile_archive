package top.xuanweiace.bili.client;

import com.alibaba.fastjson2.JSON;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import top.xuanweiace.bili.conf.BiliConf;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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

    private final String get_history_pattern = "https://api.bilibili.com/x/v2/history?pn=%d&ps=%d";
    public VideoHistoryResp getHistory(int pageSize) {
        return getHistory(1, pageSize);
    }
    public VideoHistoryResp getHistory(int pageNum, int pageSize) {
        // API 限制，在配置里，目前上限300
        if (pageSize > biliConf.maximumNumberOfVideosFetchedOnce) {
            pageSize = biliConf.maximumNumberOfVideosFetchedOnce;
        }
        String url = String.format(get_history_pattern, pageNum, pageSize);
        log.info("url={}", url);
        HttpHeaders headers = new HttpHeaders();
        List<String> cookies = new ArrayList<>();
        cookies.add(biliConf.userCookie);
        headers.put(HttpHeaders.COOKIE, cookies);
        headers.setContentType(MediaType.APPLICATION_JSON);

        HttpEntity<Map<String, Object>> entity = new HttpEntity<>(headers);
        ResponseEntity<String> resp = restTemplate.exchange(url, HttpMethod.GET, entity, String.class);

        VideoHistoryResp resp2 = JSON.parseObject(resp.getBody(), VideoHistoryResp.class);
        return resp2;
    }

}
