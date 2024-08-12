package b1nd.dodam.domain.rds.outsleeping.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OutSleepingExceptionCode implements ExceptionCode {

    INVALID_PERIOD(400, "잘못된 외박 기간"),
    NOT_APPLICANT(403, "신청자가 아님"),
    NOT_FOUND(404, "없는 외박");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
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
