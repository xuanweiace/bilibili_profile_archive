package top.xuanweiace.bili.dao;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.Resource;
import java.util.List;

/**
 *
 * 关注PO之间的转化和组合
 * @author zxz
 * @date 2023/5/12 13:58
 */

@Repository
public class VideoDao {
    @Resource
    VideoMapper videoMapper;


    //insertOrUpdate,首先直接更新，如果更新失败返回O,接着发起select,查询为0，接着才发起Insert
    @Transactional(rollbackFor = {Exception.class})
    public int insertOrUpdate(VideoPO video) {
        int ret = videoMapper.updateById(video);
        if(ret == 0) ret = videoMapper.insert(video);
        return ret;
    }

    public int batchInsertOrUpdate(List<VideoPO> videoPOs) {
        int ret = 0;
        for (VideoPO videoPO : videoPOs) {
            ret += insertOrUpdate(videoPO);
        }
        return ret;
    }

    public int insert(VideoPO video) {
        return videoMapper.insert(video);
    }

    public List<VideoPO> listVideosByLimit(int limit) {
        LambdaQueryWrapper<VideoPO> queryWrapper=new LambdaQueryWrapper();
        if (limit != -1)  {
            queryWrapper.orderByDesc(VideoPO::getView_at).last("limit 0,"+limit);
        }

        List<VideoPO> videos = videoMapper.selectList(queryWrapper);
        return videos;
    }
}
