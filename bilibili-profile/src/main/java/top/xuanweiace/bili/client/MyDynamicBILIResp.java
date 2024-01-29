package top.xuanweiace.bili.client;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;
import top.xuanweiace.bili.dao.MyDynamicPO;
import top.xuanweiace.bili.enums.PushStatus;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zxz
 * @date 2024/1/25 0:52
 */
@Data
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class MyDynamicBILIResp {

    private int code;
    private String message;
    private int ttl;
    private _Data data;

    @Data
    @ToString
    public class _Data {
        List<_Item> items;

    }

    @Data
    @ToString
    public class _Item {
        String id_str;
        _Module modules;
    }

    @Data
    @ToString
    public class _Module {
        _ModuleDynamic module_dynamic;
        _ModuleAuthor module_author;
    }
    @Data
    @ToString
    public class _ModuleAuthor {
        String mid;
        String name;
    }
    @Data
    @ToString
    public class _ModuleDynamic {
        _Major major;
    }

    @Data
    @ToString
    public class _Major {
        _Archive archive;
        String type;
    }

    @Data
    @ToString
    public class _Archive {
        String aid;
        String bvid;
        String cover;
        String desc;
        String duration_text;
        String title;
    }

    public List<MyDynamicPO> toPOs() {
        List<MyDynamicPO> li = new ArrayList<>();
        List<_Item> items = this.getData().items;
        for (_Item item : items) {
            MyDynamicPO po = new MyDynamicPO();
            po.setIdStr(item.id_str);
            po.setIsPushed(PushStatus.NOT_PUSH.getCode());
            try {
                _Archive archive = item.modules.module_dynamic.major.archive;
                po.setAid(archive.aid);
                po.setDescription(archive.desc);
                po.setBvid(archive.bvid);
                po.setCover(archive.cover);
                po.setTitle(archive.title);
            } catch (NullPointerException e) {
//                System.out.println("item.id_str = " + item.id_str + ", 的动态不包含archive");
//                if(item.modules.module_dynamic.major != null) {
//                    System.out.println("动态的类型为:" + item.modules.module_dynamic.major.type);
//                }
            }
            try {
                po.setType(item.modules.module_dynamic.major.type);
            } catch (NullPointerException e) {}


            try {
                po.setAutherName(item.modules.module_author.name);
                po.setMid(item.modules.module_author.mid);
            } catch (NullPointerException e) {
                //TODO 发送告警，是需要关注的异常。如果真到了这一步，则对【无作者的】api数据需要想办法处理
            }
            li.add(po);
        }
        return li;
    }
}

