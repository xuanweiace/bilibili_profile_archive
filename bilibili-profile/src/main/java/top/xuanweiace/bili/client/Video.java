package top.xuanweiace.bili.client;

import com.alibaba.fastjson2.JSON;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import org.springframework.beans.BeanUtils;
import top.xuanweiace.bili.common.TimeUtil;
import top.xuanweiace.bili.dao.VideoPO;
import top.xuanweiace.bili.entities.VideoVO;

/**
 * @author zxz
 * @date 2023/5/11 15:56
 */


/**
 * Video 要求字段和bilibili-api接口返回的字段名一致。
 * 一个视频的不同P，av和bv链接是一样的。可以通过url参数来区分
 * 比如：https://www.bilibili.com/video/BV1xN4y157JK?p=17
* */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Video {
    private Long aid; //视频号，表的唯一主键
    private Integer videos; // 视频分P总数 默认为1
    private Integer tid; //分区tid
    private String tname; //子分区名称
    private Integer copyright;
    private String pic; //视频封面图片url
    private String title; // 视频标题
    private Long pubdate; // 稿件发布时间
    private Long ctime;
    private String desc; // 视频简介
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


    public VideoVO toVo() {
        Owner owner = JSON.parseObject(this.owner, Owner.class);
        Page page = JSON.parseObject(this.page, Page.class);
        return new VideoVO(aid, tname, pic, title, pubdate, desc, TimeUtil.formatSeconds(Long.valueOf(duration)),
                owner.getName(), short_link_v2, page.getPart(), redirect_link, bvid);
    }
    public VideoPO toPO() {
        VideoPO video = new VideoPO();
        BeanUtils.copyProperties(this, video);
        video.setDescription(this.desc);//属性名不一致
        return video;
    }
}
//记录分辨率的，不需要
//class Dimension {
//
//    private int width;
//    private int height;
//    private int rotate;
//}
@Data
class Owner {

    private Long mid;
    private String name;
    private String face;
}
@Data
class Page {

    private Long cid;
    private Integer page;
    private String from;
    private String part;
    private Integer duration;
    private String vid;
    private String weblink;
    private String first_frame;
}

