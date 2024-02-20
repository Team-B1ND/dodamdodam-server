package b1nd.dodamcore.outsleeping.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OutSleepingExceptionCode implements ExceptionCode {

    INVALID_PERIOD(HttpStatus.BAD_REQUEST, "잘못된 외박 기간"),
    NOT_APPLICANT(HttpStatus.FORBIDDEN, "신청자가 아님"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 외박");

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