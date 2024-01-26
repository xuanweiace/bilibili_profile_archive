package top.xuanweiace.bili.Utils;

/**
 * @author zxz
 * @date 2024/1/26 15:50
 */
public class MUtils {

    static final private int LOG_CUT_LENGTH = 200;
    public static String logCut(String s) {
        return s.substring(0, Math.min(s.length(), LOG_CUT_LENGTH)) + ".......etc";
    }
}
