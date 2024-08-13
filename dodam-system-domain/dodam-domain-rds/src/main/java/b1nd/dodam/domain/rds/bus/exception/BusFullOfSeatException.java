package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusFullOfSeatException extends CustomException {

    public BusFullOfSeatException() {
        super(BusExceptionCode.BUS_FULL_OF_SEAT);
    }

}
