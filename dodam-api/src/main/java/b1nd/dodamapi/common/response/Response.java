package b1nd.dodamapi.common.response;

import lombok.Builder;
import lombok.Getter;
import org.springframework.http.HttpStatus;

@Getter
@Builder
public class Response {

    private int status;
    private String message;

    public static Response of(HttpStatus status, String message) {
        return new Response(status.value(), message);
    }
}