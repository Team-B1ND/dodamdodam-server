package b1nd.dodam.domain.rds.outgoing.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum OutGoingExceptionCode implements ExceptionCode {

    INVALID_DURATION(400, "잘못된 외출 기간"),
    NOT_APPLICANT(403, "신청자가 아님"),
    NOT_FOUND(404, "없는 외출");

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
