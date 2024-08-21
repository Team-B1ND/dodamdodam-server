package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusApplicationNotFoundException extends CustomException {

    public BusApplicationNotFoundException() {
        super(BusExceptionCode.BUS_MEMBER_NOT_FOUND);
    }

}
