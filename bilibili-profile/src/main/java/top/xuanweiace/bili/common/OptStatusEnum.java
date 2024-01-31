package top.xuanweiace.bili.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OptStatusEnum {
    SUCCESS("0000", "操作成功"),
    Business_Error("5001","业务错误"),
    UNKNOWN_ERROR("9999", "未知错误");

    final String code;

    final String description;

}
