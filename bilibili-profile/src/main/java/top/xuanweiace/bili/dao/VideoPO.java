package top.xuanweiace.bili.dao;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import top.xuanweiace.bili.client.Video;

/**
 * @author zxz
 * @date 2023/5/12 14:31
 */
@Data
@ToString
@TableName("video")
public class VideoPO {
    @TableId(value = "aid")
    private Long aid; //视频号，表的唯一主键
    private Integer videos; // 视频分P总数 默认为1
    private Integer tid; //分区tid
    private String tname; //子分区名称
    private Integer copyright;
    private String pic; //视频封面图片url
    private String title; // 视频标题
    private Long pubdate; // 稿件发布时间
    private Long ctime;
    // 还可以这么写，避免desc的关键字问题
    @TableField("`description`")
    private String description; // 视频简介
    private Integer state;
    private Integer duration; //视频总计持续时长（所有分P）
    private String owner; // 其实是Owner对象
    private String dynamic;
    private Long cid; // 视频1P cid
    private String short_link_v2; // bv号短链接
    private Integer up_from_v2;
    private String first_frame;
    private String pub_location;
    private boolean favorite; // 是否已收藏
    private Integer type; // 视频属性 3普通视频 4剧集 5课程
    private Integer sub_type; //附视频属性
    private Integer device; //观看平台代码
    private String page; // 其实是Page对象
    private Integer count; //分P数
    private Integer progress; //观看进度
    private Long view_at; //观看时间
    private Long kid; //稿件avid
    private String business;
    private String redirect_link; //重定向url
    private String bvid; //稿件bvid

    public Video toBO() {
        Video video = new Video();
        BeanUtils.copyProperties(this, video);
        video.setDesc(this.description);//属性名不一致
        return video;
    }
}
