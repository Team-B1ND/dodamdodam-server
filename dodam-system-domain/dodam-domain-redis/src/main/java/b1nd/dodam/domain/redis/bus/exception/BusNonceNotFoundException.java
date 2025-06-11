package b1nd.dodam.domain.redis.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusNonceNotFoundException extends CustomException {
    public BusNonceNotFoundException() {
        super(BusRedisExceptionCode.BUS_NONCE_NOT_FOUND);
    }

}
