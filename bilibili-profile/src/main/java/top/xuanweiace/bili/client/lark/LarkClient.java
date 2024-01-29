package top.xuanweiace.bili.client.lark;

import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import top.xuanweiace.bili.conf.LarkConf;
import top.xuanweiace.bili.dao.MyDynamicPO;

import java.io.IOException;
import java.net.URL;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.*;

/**
 * @author zxz
 * @date 2024/1/25 23:08
 */
@Component
@Slf4j
@Configuration
@EnableScheduling
public class LarkClient {

    @Autowired
    LarkConf larkConf;
    @Autowired
    RestTemplate restTemplate;

    String tenant_access_token;
    final String uploadURL = "https://open.feishu.cn/open-apis/im/v1/images";
    final String TENANT_ACCESS_TOKENURL = "https://open.feishu.cn/open-apis/auth/v3/tenant_access_token/internal";

    @Scheduled(fixedRate=600000) // 每一小时执行一次
    private void refreshTenantAccessToken() {
        log.info("refreshTenantAccessToken is running...");
        Map<String, String> requestBodyMap = new HashMap<>();
        requestBodyMap.put("app_id", larkConf.appid);
        requestBodyMap.put("app_secret", larkConf.appsecret);
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        // 创建HttpEntity对象，将请求体和请求头封装进去
        HttpEntity<Map<String, String>> requestEntity = new HttpEntity<>(requestBodyMap, headers);

        // 发起POST请求
        String url = TENANT_ACCESS_TOKENURL;
        ResponseEntity<Map> response = restTemplate.postForEntity(url, requestEntity, Map.class);
        Map<String, String> responseBody = response.getBody();
        log.info("refreshTenantAccessToken  更新前后的token为 {} 和 {} ",tenant_access_token,responseBody.get("tenant_access_token"));
        // TODO 告警机制加上
        tenant_access_token = responseBody.get("tenant_access_token");
    }
    private ByteArrayResource getImageResourceFromURL(String url) throws IOException {
        URL imageUrl = new URL(url);
        Path tempFile = Files.createTempFile("temp", ".jpg");
        Files.copy(imageUrl.openStream(), tempFile, StandardCopyOption.REPLACE_EXISTING);
        ByteArrayResource resource = new ByteArrayResource(Files.readAllBytes(tempFile));
        return resource;
    }

    //https://open.feishu.cn/document/server-docs/im-v1/image/create?appId=cli_a538d1da283cd00c
    public String uploadImg(String url) {
        if(StringUtils.isBlank(url)) {
            return "";
        }
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.MULTIPART_FORM_DATA);
        String bearer = "Bearer " + tenant_access_token;
        headers.set("Authorization", bearer);
        try {
            MultiValueMap<String, Object> map = new LinkedMultiValueMap<>();
            map.add("image_type", "message");
            map.add("image", getImageResourceFromURL(url));
            HttpEntity<MultiValueMap<String, Object>> entity = new HttpEntity<>(map, headers);
            ResponseEntity<String> response = restTemplate.exchange(uploadURL, HttpMethod.POST, entity, String.class);
            System.out.println("上传图片到Lark的返回值:"+response.getBody());
            ObjectMapper objectMapper = new ObjectMapper();
            Map<String, Object> result = objectMapper.readValue(response.getBody(), Map.class);
            String imageKey = (String) ((Map) result.get("data")).get("image_key");
            return imageKey;
        } catch (IOException e) {
            e.printStackTrace();
        }

        return "";
    }

    public boolean pushBilibiliMyDynamic(MyDynamicPO po) {
        RestTemplate restTemplate = new RestTemplate();
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String imagekey = this.uploadImg(po.getCover());

        LarkPushRequestBody requestBody = new LarkPushRequestBody();
        requestBody.setMsg_type("post");

        Content content = new Content();
        Post post = new Post();
        Zh_cn zh_cn = new Zh_cn();

        zh_cn.setTitle(String.format("[%s] %s", po.getAutherName(), po.getTitle()));

        List<ContentItem> contentItems = new ArrayList<>();

        ContentItem item1 = new ContentItem();
        item1.setTag("a");
        item1.setText("https://www.bilibili.com/video/" + po.getBvid());
        item1.setHref("https://www.bilibili.com/video/" + po.getBvid());

        ContentItem item2 = new ContentItem();
        item2.setTag("text");
        item2.setText("\n简介:\n" + po.getDescription()+"\n");

        contentItems.add(item1);
        contentItems.add(item2);
        ContentItem item3 = new ContentItem();
        if(imagekey != "") {
            item3.setTag("img");
            item3.setImage_key(imagekey);
        }
        contentItems.add(item3);


        zh_cn.setContent(new ArrayList<>(Arrays.asList(contentItems)));

        post.setZh_cn(zh_cn);
        content.setPost(post);
        requestBody.setContent(content);

        HttpEntity<LarkPushRequestBody> entity = new HttpEntity<>(requestBody, headers);
        System.out.println("entity = " + entity);
        String url = "https://open.feishu.cn/open-apis/bot/v2/hook/" + larkConf.webhook;
        ResponseEntity<String> response = restTemplate.postForEntity(url, entity, String.class);
        log.info("此次飞书推送webhook发送为:{},  返回值:{}",entity,response.getBody());

        return true;
    }

}
