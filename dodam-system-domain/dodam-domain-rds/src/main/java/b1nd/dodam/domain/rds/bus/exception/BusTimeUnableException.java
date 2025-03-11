package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusTimeUnableException extends CustomException {

    public BusTimeUnableException() {
        super(BusExceptionCode.BUS_TIME_UNABLE);
    }

}
