package b1nd.dodam.restapi.auth.infrastructure.security.support;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;

import java.util.Enumeration;

public final class TokenExtractor {

    private static final String AUTHORIZATION = "Authorization";

    private TokenExtractor() {}

    public static String extract(HttpServletRequest request, String type) {
        Enumeration<String> headers = request.getHeaders(AUTHORIZATION);

        while (headers.hasMoreElements()) {
            String value = headers.nextElement();
            if (value.toLowerCase().startsWith(type.toLowerCase())) {
                return value.substring(type.length()).trim();
            }
        }

        return Strings.EMPTY;
    }

}
