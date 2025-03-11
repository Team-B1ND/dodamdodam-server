package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusTimNotFoundException extends CustomException {

    public BusTimNotFoundException() {
        super(BusExceptionCode.BUS_TIME_NOT_FOUND);
    }

}