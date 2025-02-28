package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BusExceptionCode implements ExceptionCode {

    BUS_PERIOD_EXPIRED(410,"기간 만료된 버스"),
    BUS_FULL_OF_SEAT(400,"버스 좌석 만료"),
    BUS_ALREADY_APPLIED(409,"이미 신청한 버스"),
    BUS_MEMBER_NOT_FOUND(404, "없는 버스 멤버"),
    BUS_NOT_FOUND(404, "없는 버스"),
    BUS_ACCESS_DENIED(403, "버스 권한 부족"),
    BUS_API_KEY_INVALID(403, "버스 API 키가 유효하지 않음");

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
