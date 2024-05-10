package b1nd.dodamapi.common.exception;

import b1nd.dodamcore.common.exception.ErrorResponseEntity;
import b1nd.dodamcore.common.exception.ExceptionCode;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamcore.common.exception.CustomException;
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
        log.error("Exception : {}, {}", code.getHttpStatus(), code.getMessage());

        return ErrorResponseEntity.responseEntity(e.getExceptionCode());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    protected ResponseEntity<ErrorResponseEntity> handleValidException(MethodArgumentNotValidException e) {
        String message = getValidExceptionMessages(e.getBindingResult().getAllErrors());

        log.error("Valid Fail Object : {}", e.getObjectName());
        log.error("Valid Fail Message : \"{}\"", message);

        return ResponseEntity
                .status(e.getStatusCode())
                .body(ErrorResponseEntity.builder()
                        .status(e.getStatusCode().value())
                        .code(GlobalExceptionCode.PARAMETER_NOT_VALID.name())
                        .message(message)
                        .build()
                );
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
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.PARAMETER_NOT_FOUND.getStatus().value())
                        .code(GlobalExceptionCode.PARAMETER_NOT_FOUND.name())
                        .message(GlobalExceptionCode.PARAMETER_NOT_FOUND.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    protected ResponseEntity<ErrorResponseEntity> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.PARAMETER_NOT_FOUND.getStatus().value())
                        .code(GlobalExceptionCode.PARAMETER_NOT_FOUND.name())
                        .message(GlobalExceptionCode.PARAMETER_NOT_FOUND.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    protected ResponseEntity<ErrorResponseEntity> handleHttpRequestMethodNotSupportedException(HttpRequestMethodNotSupportedException e) {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.METHOD_NOT_SUPPORTED.getStatus().value())
                        .code(GlobalExceptionCode.METHOD_NOT_SUPPORTED.name())
                        .message(GlobalExceptionCode.METHOD_NOT_SUPPORTED.getMessage())
                        .build());
    }

    @ExceptionHandler(HttpMediaTypeNotSupportedException.class)
    protected ResponseEntity<ErrorResponseEntity> handleHttpMediaTypeNotSupportedException() {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.getStatus().value())
                        .code(GlobalExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.name())
                        .message(GlobalExceptionCode.MEDIA_TYPE_NOT_SUPPORTED.getMessage())
                        .build());
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    protected ResponseEntity<ErrorResponseEntity> handleMethodArgumentTypeMismatchException() {
        return ResponseEntity
                .status(400)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.MEDIA_TYPE_MISS_MATCHED.getStatus().value())
                        .code(GlobalExceptionCode.MEDIA_TYPE_MISS_MATCHED.name())
                        .message(GlobalExceptionCode.MEDIA_TYPE_MISS_MATCHED.getMessage())
                        .build());
    }

    @ExceptionHandler(Exception.class)
    protected ResponseEntity<ErrorResponseEntity> handleException(Exception e, HttpServletRequest request){
        sendErrorNotice(e, request);

        return ResponseEntity
                .status(500)
                .body(ErrorResponseEntity.builder()
                        .status(GlobalExceptionCode.INTERNAL_SERVER.getStatus().value())
                        .code(GlobalExceptionCode.INTERNAL_SERVER.name())
                        .message(GlobalExceptionCode.INTERNAL_SERVER.getMessage())
                        .build());
    }

    private void sendErrorNotice(Exception e, HttpServletRequest request) {
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
