package top.xuanweiace.bili.entities;

import lombok.AllArgsConstructor;
import lombok.Data;

/**
 * @author zxz
 * @date 2023/5/11 16:24
 */
@Data
@AllArgsConstructor
public class VideoVO {
    private long aid; //视频号，表的唯一主键
    private String tname; //子分区名称
    private String pic; //视频封面图片url
    private String title; // 视频标题
    private long pubdate; // 稿件发布时间
    private String desc; // 视频简介
    private String duration; //视频总计持续时长（所有分P） hh:mm:ss
    private String author_name; // 作者名（与dao.Video区分）
    private String short_link_v2; // bv号短链接
    private String part_name; // 当前part
    private String redirect_link; //重定向url
    private String bvid; //稿件bvid

}
