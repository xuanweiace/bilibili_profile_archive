package top.xuanweiace.bili.common;

import java.time.Duration;

/**
 * @author zxz
 * @date 2023/5/11 20:16
 */
public class TimeUtil {

    public static String formatSeconds(long seconds) {
        Duration duration = Duration.ofSeconds(seconds);
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int secs = duration.toSecondsPart();
        return String.format("%02d:%02d:%02d", hours, minutes, secs);
    }
}
