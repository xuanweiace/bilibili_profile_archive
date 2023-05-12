package top.xuanweiace.bili.common;

import lombok.Data;

/**
 * @author zxz
 * @date 2023/5/11 15:55
 */
@Data
public class ResponseResult {

    /**
     * 状态码
     */
    private String code;

    /**
     * 提示信息
     */
    private String msg;

    /**
     * 返回数据
     */
    private Object data;

    //get和set省略
    public static <T> ResponseResult success(T object) {
        ResponseResult result = new ResponseResult();
        result.setCode(OptStatusEnum.SUCCESS.getCode());
        result.setMsg("OK");
        result.setData(object);
        return result;
    }

    public static ResponseResult success() {
        return success(null);
    }

    public static ResponseResult error(String code, String msg) {
        ResponseResult result = new ResponseResult();
        result.setCode(code);
        result.setMsg(msg);
        return result;
    }

    public static ResponseResult error(OptStatusEnum optStatusEnum) {
        ResponseResult result = new ResponseResult();
        result.setCode(optStatusEnum.getCode());
        result.setMsg(optStatusEnum.getDescription());
        return result;
    }

}
