package top.xuanweiace.bili.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import top.xuanweiace.bili.client.BiliClient;
import top.xuanweiace.bili.client.VideoHistoryResp;
import top.xuanweiace.bili.conf.BiliConf;
import top.xuanweiace.bili.dao.VideoDao;
import top.xuanweiace.bili.dao.VideoPO;
import top.xuanweiace.bili.client.Video;
import top.xuanweiace.bili.entities.VideoVO;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zxz
 * @date 2023/5/11 22:00
 */
@Service
@Slf4j
public class HistoryService {
    @Autowired
    VideoDao videoDao;
    @Autowired
    BiliClient biliClient;

    @Autowired
    BiliConf biliConf;
    public List<VideoVO> getHistoryFromBilibili(Integer limit) {
        VideoHistoryResp history = biliClient.getHistory(limit);
        List<VideoVO> voList = history.getData().stream().map(Video::toVo).collect(Collectors.toList());
        return voList;
    }
    // 如果limit==-1，则代表是全取
    public List<VideoVO> getAllVideoFromDB(Integer limit) {
        List<VideoPO> videoPOS = videoDao.listVideosByLimit(limit);
        List<Video> videos = videoPOS.stream().map(VideoPO::toBO).collect(Collectors.toList());
        List<VideoVO> voList = videos.stream().map(Video::toVo).collect(Collectors.toList());
        return voList;
    }
    // 用biliClient爬取全量数据，并写入mysql。
    public void collectAllHistoryFromBilibili() {
        int curPageNum = 1;
        int affected_rows = 0;
        while(true) {
            VideoHistoryResp history = biliClient.getHistory(curPageNum, biliConf.maximumNumberOfVideosFetchedOnce);
            List<Video> videos = history.getData();
            if(videos == null) break;
            int st_pos = (curPageNum-1)*biliConf.maximumNumberOfVideosFetchedOnce;
            log.info("正在收集 Bilibili 历史记录... 偏移量[{}, {}]", st_pos, st_pos + videos.size());
            List<VideoPO> videoPOs = videos.stream().map(Video::toPO).collect(Collectors.toList());
            affected_rows += videoDao.batchInsertOrUpdate(videoPOs);
            if(videos.size() != biliConf.maximumNumberOfVideosFetchedOnce) {
                break;
            }
            curPageNum ++;
        }
        log.info("收集 Bilibili 历史记录 结束！ 共影响{}行", affected_rows);

    }

}
