package top.xuanweiace.bili.client;

import lombok.Data;

import java.util.List;

/**
 * @author zxz
 * @date 2023/5/11 17:34
 */
@Data
public class VideoHistoryResp {

    private int code;
    private String message;
    private int ttl;
    private List<Video> data;
}