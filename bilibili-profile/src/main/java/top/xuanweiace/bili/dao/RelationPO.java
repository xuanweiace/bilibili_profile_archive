package top.xuanweiace.bili.dao;

import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

/**
 * @author zxz
 * @date 2024/1/26 16:48
 */
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
@TableName("relation")
public class RelationPO {
    @TableId
    String mid;
    int relation;

}
