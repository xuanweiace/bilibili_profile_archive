package top.xuanweiace.bili.dao;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.dao.DuplicateKeyException;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import top.xuanweiace.bili.enums.PushStatus;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author zxz
 * @date 2024/1/25 1:24
 */
@Repository
public class MyDynamicDao extends ServiceImpl<MyDynamicMapper, MyDynamicPO> implements MyDynamicService {
    @Resource
    MyDynamicMapper myDynamicMapper;
    // 返回值没用
    @Transactional(rollbackFor = {Exception.class})
    public int batchInsert(List<MyDynamicPO> poList) {
        int res = 0;
        for (MyDynamicPO po : poList) {
            try {
                res += myDynamicMapper.insert(po);// 插入失败会抛异常还是只是返回0？我们期望他不抛异常，但是其实是会抛异常！！！！org.springframework.dao.DuplicateKeyException:
            } catch (DuplicateKeyException e) {}
        }
        return res;
    }

    public List<MyDynamicPO> selectNotPushed() {
        QueryWrapper<MyDynamicPO> wrapper = new QueryWrapper<>();
        wrapper.eq("is_pushed", PushStatus.NOT_PUSH.getCode());
        return myDynamicMapper.selectList(wrapper);
    }
    public MyDynamicPO selectOneByAutherName(String name) {
        QueryWrapper<MyDynamicPO> wrapper = new QueryWrapper<>();
        wrapper.eq("auther_name",name);
//        return myDynamicMapper.selectOne(wrapper);TODO 为什么不行？多条记录会报错：com.baomidou.mybatisplus.core.exceptions.MybatisPlusException: One record is expected, but the query result is multiple records
        List<MyDynamicPO> pos = myDynamicMapper.selectList(wrapper);
        if(pos != null && pos.size() >= 1) {
            return pos.get(0);
        }
        return null;
    }

//    //TODO 调试好
//    public List<MyDynamicPO> selectNotPushed(List<MyDynamicPO> poList) {
////        System.out.println("poList = " + poList);
////        System.out.println("poList.stream()\n                .map(MyDynamicPO::getId_str) = " + poList.stream()
////                .map(MyDynamicPO::getId_str));
//        System.out.println("--------------------------------------------------");
//        MyDynamicPO po = myDynamicMapper.selectById("890565613504692248");
//        List<MyDynamicPO> list = myDynamicMapper.selectBatchIds(poList.stream()
//                .map(MyDynamicPO::getIdStr)
//                .collect(Collectors.toList()));
////        System.out.println("selectNotPushed:"+ poList.size() + " + " + list.size() + poList.get(0) + "-----------------" + list.get(0));
//        return list;
//    }

    public void batchUpdate(List<MyDynamicPO> poList) {
        this.updateBatchById(poList);
    }
}
