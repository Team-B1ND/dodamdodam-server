package b1nd.dodamcore.common.exception;

import org.springframework.http.HttpStatus;

public interface ExceptionCode {

    HttpStatus getHttpStatus();
    String getExceptionName();
    String getMessage();
}
