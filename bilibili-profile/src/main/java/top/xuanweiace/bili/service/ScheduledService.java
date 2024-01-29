package top.xuanweiace.bili.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.xuanweiace.bili.client.BiliClient;
import top.xuanweiace.bili.client.MyDynamicBILIResp;
import top.xuanweiace.bili.client.VideoHistoryResp;
import top.xuanweiace.bili.conf.BiliConf;
import top.xuanweiace.bili.dao.MyDynamicDao;
import top.xuanweiace.bili.dao.MyDynamicPO;
import top.xuanweiace.bili.dao.VideoDao;
import top.xuanweiace.bili.client.Video;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zxz
 * @date 2023/5/11 22:27
 */

@Configuration      //1.主要用于标记配置类，兼备Component的效果。
@EnableScheduling   // 2.开启定时任务
@Slf4j
public class ScheduledService {

    @Autowired
    BiliClient biliClient;
    @Autowired
    BiliConf biliConf;

    @Autowired
    VideoDao videoDao;

    @Autowired
    MyDynamicDao myDynamicDao;

    @Autowired
    PushService pushService;
    //单线程执行，一轮任务执行完后开始计时
//    @Scheduled(fixedRate=60000) // 每一分钟执行一次
    @Scheduled(cron="0 0 23 * * ?")
    private void batchFetchHistoryTasks() {
        log.info("batchFetchVideoesTasks in running...");
        VideoHistoryResp history = biliClient.getHistory(biliConf.numberOfVideosFetchedFromBilibiliPerMinute);
        if (history == null || history.getData() == null) {
            log.warn("[batchFetchVideoesTasks] 爬取内容为空");
        }
        List<Video> videoList = history.getData();
//        List<Video> videoList = new ArrayList<>();
//        Video v = new Video();
//        v.setAid(7L);
//        v.setDesc("qq");
//        v.setState(123);
//        v.setPage("haha");
//        v.setBusiness("bussd");
//        videoList.add(v);
        log.info("len of videoList {}", videoList.size());
        try {
            videoList.forEach(video -> videoDao.insertOrUpdate(video.toPO()));
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        //        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }

    // 批量获取最新动态
    @Scheduled(fixedRate=60000) // 每一分钟执行一次
    private void batchFetchMyDynamicTask() {
        log.info("batchFetchMyDynamicTask in running...");
        MyDynamicBILIResp resp = biliClient.getMyDynamic();
        if (resp == null) {
            log.warn("[batchFetchMyDynamicTask] 爬取内容为空");
        }
        List<MyDynamicPO> myDynamicPOS = resp.toPOs();
        // 0. 筛选视频动态
        List<MyDynamicPO> filteredList = myDynamicPOS.stream()
                .filter(po -> StringUtils.isNotBlank(po.getAid()))
                .collect(Collectors.toList());
        // 1. 将新动态存入数据库
        myDynamicDao.batchInsert(filteredList);
        // 2. 找到DB中未推送的
        List<MyDynamicPO> needPushes = myDynamicDao.selectNotPushed();

        // 3. 将这些动态逐一推送到Lark
        List<MyDynamicPO> succPushes = pushService.pushMyDynamicToLarkOnce(needPushes);

        // 4. 维护db信息
        succPushes.stream().forEach(dynamic -> dynamic.setIsPushed(1));
        myDynamicDao.batchUpdate(succPushes);

    }
}
