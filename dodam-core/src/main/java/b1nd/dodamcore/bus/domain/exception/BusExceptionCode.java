package b1nd.dodamcore.bus.domain.exception;

import b1nd.dodamcore.common.exception.ExceptionCode;
import lombok.AllArgsConstructor;
import org.springframework.http.HttpStatus;

@AllArgsConstructor
public enum BusExceptionCode implements ExceptionCode {

    BUS_PERIOD_EXPIRED(HttpStatus.GONE,"기간 만료된 버스"),
    BUS_FULL_OF_SEAT(HttpStatus.BAD_REQUEST,"버스 좌석 만료"),
    BUS_ALREADY_APPLIED(HttpStatus.CONFLICT,"이미 신청한 버스"),
    BUS_MEMBER_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 버스 멤버"),
    BUS_NOT_FOUND(HttpStatus.NOT_FOUND, "없는 버스");

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
