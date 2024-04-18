package b1nd.dodamcore.common.util;

public class HtmlConverter {

    public static String of(String original) {
        return original
                .replaceAll("&#39;", "'");
    }
}
