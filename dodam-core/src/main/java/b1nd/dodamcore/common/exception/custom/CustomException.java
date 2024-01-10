package b1nd.dodamcore.common.exception.custom;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
@RequiredArgsConstructor
public class CustomException extends RuntimeException {

    private final ExceptionCode exceptionCode;
}
