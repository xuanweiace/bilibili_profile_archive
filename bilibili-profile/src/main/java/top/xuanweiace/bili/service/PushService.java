package top.xuanweiace.bili.service;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xuanweiace.bili.client.lark.LarkClient;
import top.xuanweiace.bili.dao.MyDynamicDao;
import top.xuanweiace.bili.dao.MyDynamicPO;
import top.xuanweiace.bili.dao.RelationMapper;
import top.xuanweiace.bili.dao.RelationPO;
import top.xuanweiace.bili.enums.PushStatus;
import top.xuanweiace.bili.enums.RelationEnum;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zxz
 * @date 2024/1/25 2:07
 */
@Service
@Slf4j
public class PushService {

    @Autowired
    LarkClient larkClient;

    @Resource
    RelationMapper relationMapper;

    @Autowired
    MyDynamicDao myDynamicDao;
    // 返回本次推送成功的
    public List<MyDynamicPO> pushMyDynamicToLarkOnce(List<MyDynamicPO> myDynamicPOs) {
        List<MyDynamicPO> succ = new ArrayList<>();
        QueryWrapper<RelationPO> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("relation", RelationEnum.BLACK_LIST.getCode());
        List<RelationPO> blackListPOS = relationMapper.selectList(queryWrapper);
        List<String> blackList = blackListPOS.stream().map(RelationPO::getMid).collect(Collectors.toList());
        List<MyDynamicPO> quitPush = new ArrayList<>();
        for (MyDynamicPO myDynamicPO : myDynamicPOs) {
            if (blackList.contains(myDynamicPO.getMid())) {// hitBlackList
                quitPush.add(myDynamicPO);
                continue;
            }
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
        quitPush.stream().forEach(q->q.setIsPushed(PushStatus.Temporarily_Abandoned_PUSH.getCode()));
        myDynamicDao.batchUpdate(quitPush);

        log.info("本次推送完成!期望推送 {} 条动态，命中黑名单 {} 条动态，推送成功 {} 条动态",myDynamicPOs.size(),quitPush.size(),succ.size());
        return succ;
    }
}
