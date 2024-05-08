package b1nd.dodaminfra.token;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;

import java.util.Enumeration;

class TokenExtractor {

    private static final String AUTHORIZATION = "Authorization";

    private TokenExtractor() {}

    static String extract(HttpServletRequest request, String type) {
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
