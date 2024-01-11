package b1nd.dodamcore.common.exception.handler;

import b1nd.dodamcore.common.exception.ErrorResponseEntity;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamcore.common.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handleCustomException(CustomException e){
        return ErrorResponseEntity.responseEntity(e.getExceptionCode());
    }

    @ExceptionHandler({MethodArgumentNotValidException.class})
    protected ResponseEntity handleValidException(MethodArgumentNotValidException e) {
        Map<String, String> errors = new HashMap<>();

        e.getBindingResult().getAllErrors().forEach(
                error -> errors.put(((FieldError) error).getField(), error.getDefaultMessage())
        );

        return ResponseEntity
                .status(e.getStatusCode())
                .body(errors);
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity handleException(Exception e){
        log.error(e.toString());
        return ResponseEntity
                .status(500)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.INTERNAL_SERVER.getStatus().value())
                        .code(GlobalExceptionCode.INTERNAL_SERVER.name())
                        .message(e.getMessage())
                        .build());
    }
}