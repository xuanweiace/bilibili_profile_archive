package top.xuanweiace.bili.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xuanweiace.bili.client.lark.LarkClient;
import top.xuanweiace.bili.dao.MyDynamicPO;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxz
 * @date 2024/1/25 2:07
 */
@Service
@Slf4j
public class PushService {

    @Autowired
    LarkClient larkClient;
    // 返回本次推送成功的
    public List<MyDynamicPO> pushMyDynamicToLarkOnce(List<MyDynamicPO> myDynamicPOs) {
        List<MyDynamicPO> succ = new ArrayList<>();
        for (MyDynamicPO myDynamicPO : myDynamicPOs) {
            boolean successPush = false;
            try {
                successPush = larkClient.pushBilibiliMyDynamic(myDynamicPO);
            } catch (Exception e) {
                log.warn("推送失败:{}，原因是{}", myDynamicPO, e);
            }
            if (successPush) {//TODO 这里优化更优写法
                succ.add(myDynamicPO);
            }
        }

        log.info("本次推送完成!期望推送 {} 条动态，推送成功 {} 条动态",myDynamicPOs.size(),succ.size());
        return succ;
    }
}
