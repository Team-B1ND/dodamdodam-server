package b1nd.dodaminfra.security.token;

import jakarta.servlet.http.HttpServletRequest;
import org.apache.logging.log4j.util.Strings;

import java.util.Enumeration;

abstract class TokenExtractor {

    static final String AUTHORIZATION = "Authorization";

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