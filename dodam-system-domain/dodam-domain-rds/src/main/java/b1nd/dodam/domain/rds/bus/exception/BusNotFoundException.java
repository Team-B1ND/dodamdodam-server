package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusNotFoundException extends CustomException {

    public BusNotFoundException() {
        super(BusExceptionCode.BUS_NOT_FOUND);
    }

}
