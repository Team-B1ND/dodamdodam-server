package b1nd.dodam.restapi.wakeupsong.application.support;

public final class HtmlConverter {

    private HtmlConverter() {}

    public static String of(String original) {
        return original
                .replaceAll("&#39;", "'");
    }

}
