package b1nd.dodam.restapi.support.exception;

import b1nd.dodam.core.exception.CustomException;
import b1nd.dodam.core.exception.ExceptionCode;
import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.restapi.support.data.ErrorResponseEntity;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.web.HttpMediaTypeNotSupportedException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.MissingServletRequestParameterException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.List;

@RestControllerAdvice
@Slf4j
@RequiredArgsConstructor
public class CustomExceptionHandler {

    private final ErrorNoticeSender errorNoticeSender;

    @ExceptionHandler(CustomException.class)
    protected ResponseEntity<ErrorResponseEntity> handleCustomException(CustomException e){
        ExceptionCode code = e.getExceptionCode();
        log.error("Exception : {}, {}", code.getStatus(), code.getMessage());

        return ErrorResponseEntity.responseEntity(e.getExceptionCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> handleValidException(MethodArgumentNotValidException e) {
        String message = getValidExceptionMessages(e.getBindingResult().getAllErrors());

        log.error("Valid Fail Object : {}", e.getObjectName());
        log.error("Valid Fail Message : \"{}\"", message);

        ErrorResponseEntity.of(e.getStatusCode().value(), GlobalExceptionCode.PARAMETER_NOT_VALID.name(),message);
        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponseEntity.of(
                        e.getStatusCode().value(),
                        GlobalExceptionCode.PARAMETER_NOT_VALID.name(),
                        message
                ));
    }

    private String getValidExceptionMessages(List<ObjectError> errors) {
        StringBuilder message = new StringBuilder();
        errors.forEach(
                error -> message
                        .append(((FieldError) error).getField()).append(" ")
                        .append(error.getDefaultMessage()).append(", ")
        );
        return message.substring(0, message.length() - 2);
    }

    @ExceptionHandler(MissingServletRequestParameterException.class)
    protected ResponseEntity<ErrorResponseEntity> handleMissingServletRequestParameterException(MissingServletRequestParameterException e) {

        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.of(
                        GlobalExceptionCode.PARAMETER_NOT_FOUND.getStatus(),
                        GlobalExceptionCode.PARAMETER_NOT_FOUND.name(),
                        GlobalExceptionCode.PARAMETER_NOT_FOUND.getMessage()
                ));
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseEntity> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.of(
                        GlobalExceptionCode.PARAMETER_NOT_FOUND.getStatus(),
                        GlobalExceptionCode.PARAMETER_NOT_FOUND.name(),
                        GlobalExceptionCode.PARAMETER_NOT_FOUND.getMessage()
                ));
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseEntity> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.of(
                        GlobalExceptionCode.METHOD_NOT_SUPPORTED.getStatus(),
                        GlobalExceptionCode.METHOD_NOT_SUPPORTED.name(),
                        GlobalExceptionCode.METHOD_NOT_SUPPORTED.getMessage()
                ));
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponseEntity> handleHttpMediaTypeNotSupportedException() {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.of(
                        GlobalExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.getStatus(),
                        GlobalExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.name(),
                        GlobalExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.getMessage()
                ));
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseEntity> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.of(
                        GlobalExceptionCode.MEDIA_TYPE_MISS_MATCHED.getStatus(),
                        GlobalExceptionCode.MEDIA_TYPE_MISS_MATCHED.name(),
                        GlobalExceptionCode.MEDIA_TYPE_MISS_MATCHED.getMessage()
                ));
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseEntity> handleException(Exception e, HttpServletRequest request) {
        sendErrorNotice(e, request);
        return ResponseEntity
                .status(500)
                .body(ErrorResponseEntity.of(
                        GlobalExceptionCode.INTERNAL_SERVER.getStatus(),
                        GlobalExceptionCode.INTERNAL_SERVER.name(),
                        GlobalExceptionCode.INTERNAL_SERVER.getMessage()
                ));
    }

    private void sendErrorNotice(Exception e, HttpServletRequest request) {
        log.info("{}", e.getMessage());
        errorNoticeSender.send(
                e,
                new RequestInfo(
                        request.getMethod(),
                        request.getRequestURI(),
                        request.getQueryString(),
                        request.getRemoteHost()
                )
        );
    }

}
