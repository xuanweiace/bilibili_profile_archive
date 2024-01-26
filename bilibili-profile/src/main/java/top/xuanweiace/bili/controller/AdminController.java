package top.xuanweiace.bili.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuanweiace.bili.client.lark.LarkClient;
import top.xuanweiace.bili.dao.MyDynamicPO;

/**
 * @author zxz
 * @date 2023/5/11 15:43
 */
@RestController()
@RequestMapping("/v1/admin")
public class AdminController {

    @Autowired
    LarkClient larkClient;
    @GetMapping("/test")
    public boolean get() {
        MyDynamicPO po = new MyDynamicPO();
        po.setBvid("BV1ac411x7Bq");
        po.setTitle("这是标题");
        po.setCover("https://i2.hdslb.com/bfs/face/ecbfb2429351cd78532608d4b48d510c9f4a5b4f.jpg");
        return larkClient.pushBilibiliMyDynamic(po);
//        return larkClient.uploadImg("https://i2.hdslb.com/bfs/face/ecbfb2429351cd78532608d4b48d510c9f4a5b4f.jpg");
    }
}
