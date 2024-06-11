package b1nd.dodamapi.common.response;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.Getter;
import org.springframework.http.ResponseEntity;

@Getter
public class ErrorResponseEntity extends Response {
    private final String code;

    private ErrorResponseEntity(int status, String code, String message) {
        super(status, message);
        this.code = code;
    }

    public static ResponseEntity<ErrorResponseEntity> responseEntity(ExceptionCode e){
        return ResponseEntity
                .status(e.getHttpStatus())
                .body(new ErrorResponseEntity(
                        e.getHttpStatus().value(),
                        e.getExceptionName(),
                        e.getMessage()));
    }

    public static ErrorResponseEntity of(int status, String code, String message) {
        return new ErrorResponseEntity(status, code, message);
    }
}
