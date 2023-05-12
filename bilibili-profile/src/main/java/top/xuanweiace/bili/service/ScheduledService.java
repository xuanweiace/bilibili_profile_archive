package top.xuanweiace.bili.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import top.xuanweiace.bili.client.BiliClient;
import top.xuanweiace.bili.client.VideoHistoryResp;
import top.xuanweiace.bili.conf.BiliConf;
import top.xuanweiace.bili.dao.VideoDao;
import top.xuanweiace.bili.dao.VideoMapper;
import top.xuanweiace.bili.dao.VideoMapper2;
import top.xuanweiace.bili.entities.Video;

import javax.annotation.Resource;
import java.util.ArrayList;
import java.util.List;

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

    @Resource
    VideoMapper videoMapper;
    @Autowired
    VideoDao videoDao;
    //单线程执行，一轮任务执行完后开始计时
    @Scheduled(fixedRate=60000) // 每一分钟执行一次
    private void batchFetchVideoesTasks() {
        log.info("batchFetchVideoesTasks in running...");
        VideoHistoryResp history = biliClient.getHistory(biliConf.numberOfVideosFetchedFromBilibiliPerMinute);
        List<Video> videoList = history.getData();
//        List<Video> videoList = new ArrayList<>();
//        Video v = new Video();
//        v.setAid(7L);
//        v.setDesc("qq");
//        v.setState(123);
//        v.setPage("haha");
//        v.setBusiness("bussd");
//        videoList.add(v);
//        log.info("len of videoList {}", videoList.size());
        try {
            videoList.forEach(video -> videoDao.insertOrUpdate(video.toPO()));
        } catch (Exception e) {
            System.out.println("e.getMessage() = " + e.getMessage());
        }

        //        System.err.println("执行静态定时任务时间: " + LocalDateTime.now());
    }
}
