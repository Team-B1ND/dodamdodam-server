package b1nd.dodam.restapi.support.data;

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

    public static Response ok(String message) {
        return new Response(HttpStatus.OK.value(), message);
    }

    public static Response created(String message) {
        return new Response(HttpStatus.CREATED.value(), message);
    }

    public static Response noContent(String message) {
        return new Response(HttpStatus.NO_CONTENT.value(), message);
    }

}
