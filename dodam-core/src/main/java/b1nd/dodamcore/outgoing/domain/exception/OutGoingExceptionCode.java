package b1nd.dodamcore.outgoing.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;

@RequiredArgsConstructor
public enum OutGoingExceptionCode implements ExceptionCode {

    INVALID_DURATION(HttpStatus.BAD_REQUEST, "잘못된 외출 기간"),
    NOT_APPLICANT(HttpStatus.FORBIDDEN, "신청자가 아님"),
    NOT_FOUND(HttpStatus.NOT_FOUND, "없는 외출");

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