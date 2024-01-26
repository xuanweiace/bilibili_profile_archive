package top.xuanweiace.bili.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zxz
 * @date 2024/1/25 1:19
 */
public enum PushStatus {
    NOT_PUSH("未推送", 0),
    PUSHED_LARK("已推送到飞书", 1);
//    PUSHED_WX("已推送到微信", 2);

    private final String text;

    private final Integer code;

    PushStatus(String text, Integer code) {
        this.text = text;
        this.code = code;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.code).collect(Collectors.toList());
    }

    public static PushStatus getEnumByCode(int code) {
        for (PushStatus e : PushStatus.values()) {
            if (e.code == code) {
                return e;
            }
        }
        return null;
    }

    public int getCode() {
        return code;
    }

    public String getText() {
        return text;
    }
}
