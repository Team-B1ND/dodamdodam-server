package b1nd.dodamcore.common.util;

public final class ModifyUtil {

    private ModifyUtil() {}

    public static <T> T modifyIfNotNull(T newValue, T defaultValue) {
        return newValue != null ? newValue : defaultValue;
    }

    public static int modifyIfNotZero(int newValue, int defaultValue) {
        return newValue != 0 ? newValue : defaultValue;
    }

    public static Integer modifyIfNotZeroPermitNull(Integer newValue, Integer defaultValue) {
        return newValue == null || newValue != 0 ? newValue : defaultValue;
    }
}

