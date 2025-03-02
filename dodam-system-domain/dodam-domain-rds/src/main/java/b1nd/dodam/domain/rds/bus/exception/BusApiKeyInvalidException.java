package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusApiKeyInvalidException extends CustomException {

    public BusApiKeyInvalidException() {
        super(BusExceptionCode.BUS_API_KEY_INVALID);
    }

}
