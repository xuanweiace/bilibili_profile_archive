package top.xuanweiace.bili.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import top.xuanweiace.bili.client.BiliClient;
import top.xuanweiace.bili.client.lark.LarkClient;
import top.xuanweiace.bili.common.OptStatusEnum;
import top.xuanweiace.bili.common.ResponseResult;
import top.xuanweiace.bili.dao.MyDynamicDao;
import top.xuanweiace.bili.dao.MyDynamicPO;
import top.xuanweiace.bili.dao.RelationMapper;
import top.xuanweiace.bili.dao.RelationPO;
import top.xuanweiace.bili.enums.RelationEnum;

import javax.annotation.Resource;

/**
 * @author zxz
 * @date 2023/5/11 15:43
 */
@RestController()
@RequestMapping("/v1/admin")
@Slf4j
public class AdminController {

    @Autowired
    LarkClient larkClient;
    @Autowired
    BiliClient biliClient;

    @Resource
    RelationMapper relationMapper;
    @Autowired
    MyDynamicDao myDynamicDao;


    @GetMapping("/test")
    public boolean get() {
//        biliClient.refreshCookie();
        log.info("biliClient.cookies = " + biliClient.cookies);
//        MyDynamicPO po = new MyDynamicPO();
//        po.setBvid("BV1ac411x7Bq");
//        po.setTitle("这是标题");
//        po.setCover("https://i2.hdslb.com/bfs/face/ecbfb2429351cd78532608d4b48d510c9f4a5b4f.jpg");
//        return larkClient.pushBilibiliMyDynamic(po);
//        return larkClient.uploadImg("https://i2.hdslb.com/bfs/face/ecbfb2429351cd78532608d4b48d510c9f4a5b4f.jpg");
        return true;
    }

    @GetMapping("/get_bili_cookie")
    public ResponseResult get_bili_cookie() {
        biliClient.refreshCookie();
        System.out.println("biliClient.cookies = " + biliClient.cookies);
        return ResponseResult.success(biliClient.cookies);
    }


    // 添加up主进入黑名单，不再接受推送
    @PostMapping("/blacklist")
    public ResponseResult setUPToBlackList(@RequestBody String uname) { //TODO 后续还是改成param吧
        System.out.println("uname = " + uname);
        MyDynamicPO po = myDynamicDao.selectOneByAutherName(uname);
        if(po == null) {
            return ResponseResult.error(OptStatusEnum.Business_Error.getCode(), "add blacklist failed");
        }
        String mid = po.getMid();
        RelationPO relationPO = relationMapper.selectById(mid);
        if(relationPO != null && relationPO.getRelation() == RelationEnum.BLACK_LIST.getCode()) {
            return ResponseResult.error(OptStatusEnum.Business_Error.getCode(), "It's already on the blacklist");
        }
        if(relationPO == null) {
            relationMapper.insert(new RelationPO(mid, RelationEnum.BLACK_LIST.getCode()));
        } else {
            relationMapper.updateById(new RelationPO(mid, RelationEnum.BLACK_LIST.getCode()));
        }

        return ResponseResult.success();
    }


    // 添加up主进入白名单，恢复接受推送
    @PostMapping("/whitelist")
    public ResponseResult setUPToWhiteList(@RequestBody String uname) {
        System.out.println("uname = " + uname);
        MyDynamicPO po = myDynamicDao.selectOneByAutherName(uname);
        if(po == null) {
            return ResponseResult.error(OptStatusEnum.Business_Error.getCode(), "add blacklist failed");
        }
        String mid = po.getMid();
        RelationPO relationPO = relationMapper.selectById(mid);
        if(relationPO != null && relationPO.getRelation() == RelationEnum.WHITE_LIST.getCode()) {
            return ResponseResult.error(OptStatusEnum.Business_Error.getCode(), "It's already on the whitelist");
        }
        if(relationPO == null) {
            relationMapper.insert(new RelationPO(mid, RelationEnum.WHITE_LIST.getCode()));
        } else {
            relationMapper.updateById(new RelationPO(mid, RelationEnum.WHITE_LIST.getCode()));
        }

        return ResponseResult.success();
    }

    //TODO 模糊查询，在发来的text里找到up主的mid并进行白名单操作
    @PostMapping("/FuzzyWhitelist")
    public ResponseResult fuzzySetUPToWhitelist(@RequestBody String text) {
        System.out.println("text = " + text);
        return ResponseResult.success();
    }


}
