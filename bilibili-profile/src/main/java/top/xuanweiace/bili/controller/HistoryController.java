package top.xuanweiace.bili.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import top.xuanweiace.bili.client.BiliClient;
import top.xuanweiace.bili.common.ResponseResult;
import top.xuanweiace.bili.entities.VideoVO;
import top.xuanweiace.bili.service.HistoryService;

import java.util.List;


/**
 *
 *
 * @author zxz
 * @date 2023/5/11 15:46
 */
@RestController
@Slf4j
@RequestMapping("/history")
public class HistoryController {

    @Autowired
    BiliClient biliClient;

    @Autowired
    HistoryService historyService;

    @GetMapping("getHistory")
    public ResponseResult getHistory(Integer limit) {
        List<VideoVO> voList = historyService.getHistoryFromBilibili(limit);

        return ResponseResult.success(voList);
    }

    @GetMapping("/archive/getHistory")

    public ResponseResult getArchiveHistory(Integer limit) {
        List<VideoVO> voList = historyService.getAllVideoFromDB(limit);

        return ResponseResult.success(voList);
    }


    @GetMapping("/collect_all")
    public ResponseResult collectAllHistory() {
        historyService.collectAllHistoryFromBilibili();
        return ResponseResult.success();
    }
}
