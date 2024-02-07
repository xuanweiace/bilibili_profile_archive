package top.xuanweiace.bili.conf;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

/**
 * @author zxz
 * @date 2023/5/11 17:40
 */
@Configuration
public class BiliConf {

    @Value(value = "${bili.user-cookie}")
    public String userCookie;
    @Value(value="${bili.number-of-videos-fetched-perMinute}")
    public Integer numberOfVideosFetchedFromBilibiliPerMinute;

    @Value("${bili.maximum-number-of-videos-fetched-once}")
    public Integer maximumNumberOfVideosFetchedOnce;

    @Value("${bili.ac_time_value}")
    public String ac_time_value;

    public String getRefreshToken() {
        return ac_time_value;
    }
    public void setRefreshToken(String newToken) {
        ac_time_value = newToken;
    }

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
