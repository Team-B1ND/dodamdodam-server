package b1nd.dodamcore.common.util;

import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;

public final class ZonedDateTimeUtil {

    private ZonedDateTimeUtil() {}

    public static LocalDateTime nowToLocalDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public static LocalTime nowToLocalTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalTime();
    }

}