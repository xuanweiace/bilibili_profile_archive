package top.xuanweiace.bili.client.lark;

import lombok.Data;
import lombok.ToString;

/**
 * @author zxz
 * @date 2024/1/25 23:19
 */
@Data
@ToString
public class LarkPushRequestBody {
    private String msg_type;
    private Content content;

    public String getMsg_type() {
        return msg_type;
    }

    public void setMsg_type(String msg_type) {
        this.msg_type = msg_type;
    }

    public Content getContent() {
        return content;
    }

    public void setContent(Content content) {
        this.content = content;
    }
}
@Data
@ToString
class Content {
    private Post post;
}
@Data
@ToString
class Post {
    private Zh_cn zh_cn;

    public Zh_cn getZh_cn() {
        return zh_cn;
    }

    public void setZh_cn(Zh_cn zh_cn) {
        this.zh_cn = zh_cn;
    }
}
@Data
@ToString
class Zh_cn {
    private String title;
    private ContentItem[][] content;
}
@Data
@ToString
class ContentItem {
    private String tag;
    private String text;
    private String href;
    private String image_key;
    public ContentItem() {
        tag = "text";
        text = "";
    }
}
