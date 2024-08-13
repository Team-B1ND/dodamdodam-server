package b1nd.dodam.core.util;

import java.time.*;

public final class ZonedDateTimeUtil {

    private ZonedDateTimeUtil() {}

    public static LocalDateTime nowToLocalDateTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDateTime();
    }

    public static LocalDate nowToLocalDate() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalDate();
    }

    public static LocalTime nowToLocalTime() {
        return ZonedDateTime.now(ZoneId.of("Asia/Seoul")).toLocalTime();
    }

}
