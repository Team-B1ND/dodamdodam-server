package b1nd.dodamcore.banner.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum BannerExceptionCode implements ExceptionCode {

    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 배너");

    private final HttpStatus status;
    private final String message;

    @Override
    public HttpStatus getHttpStatus() {
        return this.status;
    }

    @Override
    public String getExceptionName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}