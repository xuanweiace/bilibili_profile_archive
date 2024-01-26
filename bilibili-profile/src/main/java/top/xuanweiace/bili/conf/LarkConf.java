package top.xuanweiace.bili.conf;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

/**
 * @author zxz
 * @date 2024/1/25 23:45
 */
@Configuration
public class LarkConf {
    @Value(value = "${lark.Bearer}")
    public String bearer;

    @Value(value = "${lark.webhook}")
    public String webhook;

    @Value(value = "${lark.appid}")
    public String appid;

    @Value(value = "${lark.appsecret}")
    public String appsecret;
}
