package b1nd.dodam.core.util;

import java.time.LocalDateTime;

public final class NullSafeParserUtil {

    private NullSafeParserUtil() {}

    public static Long parseLong(String value) {
        try {
            return (value != null) ? Long.parseLong(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static Integer parseInt(String value) {
        try {
            return (value != null) ? Integer.parseInt(value) : null;
        } catch (NumberFormatException e) {
            return null;
        }
    }

    public static LocalDateTime parseLocalDateTime(String value) {
        if (value == null) {
            return null;
        }
        try {
            return LocalDateTime.parse(value);
        } catch (Exception e) {
            return null;
        }
    }
}
