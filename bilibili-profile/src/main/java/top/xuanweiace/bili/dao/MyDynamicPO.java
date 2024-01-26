package top.xuanweiace.bili.dao;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.io.Serializable;

/**
 * @author zxz
 * @date 2024/1/25 0:50
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("my_dynamic")
public class MyDynamicPO implements Serializable {
    @TableId(value = "id_str",type = IdType.ASSIGN_ID)
    String idStr;
    String aid;
    String mid;
    @TableField("`auther_name`")
    String autherName;
    String bvid;
    String cover;
    String title;
    String type;
    String description;
    @TableField("`is_pushed`")
    Integer isPushed;
}
