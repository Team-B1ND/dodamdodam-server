package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.AllArgsConstructor;

@AllArgsConstructor
public enum BusExceptionCode implements ExceptionCode {
    BUS_NOT_FOUND(404, "버스를 찾을 수 없습니다."),
    BUS_APPLICANT_NOT_FOUND(404, "버스 신청자가 아닙니다."),
    BUS_ALREADY_APPLIED_POSITION(400, "이미 신청되어있는 자리입니다.")
    ;

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return status;
    }

    @Override
    public String getExceptionName() {
        return name();
    }

    @Override
    public String getMessage() {
        return message;
    }
}
