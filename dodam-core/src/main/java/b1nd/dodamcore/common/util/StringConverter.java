package b1nd.dodamcore.common.util;

public class StringConverter {

    public static String of(String originalString) {
        return originalString.replaceAll("[0-9].", "")
                .replaceAll("[()]", "")
                .replaceAll("\\.", "")
                .replaceAll("  ", "")
                .replaceAll("<br/>", ", ");
    }
}
