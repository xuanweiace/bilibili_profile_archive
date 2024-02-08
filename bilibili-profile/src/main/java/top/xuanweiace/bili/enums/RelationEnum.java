package top.xuanweiace.bili.enums;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zxz
 * @date 2024/1/30 2:25
 */
public enum RelationEnum {
    UNKNOWN("未知", 0),
    WHITE_LIST("白名单",1),
    BLACK_LIST("黑名单", 2);

    private final String text;

    private final Integer code;

    RelationEnum(String text, Integer code) {
        this.text = text;
        this.code = code;
    }

    public static List<Integer> getValues() {
        return Arrays.stream(values()).map(item -> item.code).collect(Collectors.toList());
    }

    public static RelationEnum getEnumByCode(int code) {
        for (RelationEnum e : RelationEnum.values()) {
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

