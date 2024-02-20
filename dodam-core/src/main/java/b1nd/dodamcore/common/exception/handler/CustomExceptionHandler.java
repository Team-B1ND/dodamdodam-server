package b1nd.dodamcore.common.exception.handler;

import b1nd.dodamcore.common.exception.ErrorResponseEntity;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamcore.common.exception.custom.CustomException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity handleCustomException(CustomException e){
        return ErrorResponseEntity.responseEntity(e.getExceptionCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity handleValidException(MethodArgumentNotValidException e) {
        StringBuilder message = new StringBuilder();
        e.getBindingResult().getAllErrors().forEach(
                error -> message
                        .append(((FieldError) error).getField()).append(" ")
                        .append(error.getDefaultMessage()).append(", ")
        );

        log.error("Valid Fail Object : {}", e.getObjectName());
        log.error("Valid Fail Message : \"{}\"", message.substring(0, message.length() - 2));

        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponseEntity.builder()
                        .status(e.getStatusCode().value())
                        .code(GlobalExceptionCode.PARAMETER_NOT_VALID.name())
                        .message(message.substring(0, message.length() - 2))
                        .build()
                );
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        log.error(e.toString());

        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.PARAMETER_NOT_FOUND.getStatus().value())
                        .code(GlobalExceptionCode.PARAMETER_NOT_FOUND.name())
                        .message(GlobalExceptionCode.PARAMETER_NOT_FOUND.getMessage())
                        .build());
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