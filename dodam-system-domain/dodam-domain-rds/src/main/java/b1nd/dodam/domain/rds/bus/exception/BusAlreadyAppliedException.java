package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusAlreadyAppliedException extends CustomException {

    public BusAlreadyAppliedException() {
        super(BusExceptionCode.BUS_ALREADY_APPLIED);
    }

}
