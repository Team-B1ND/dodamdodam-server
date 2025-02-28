package b1nd.dodam.domain.redis.bus.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum BusRedisExceptionCode implements ExceptionCode {

    BUS_NONCE_NOT_FOUND(404, "버스 난수를 찾을 수 없음");


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
