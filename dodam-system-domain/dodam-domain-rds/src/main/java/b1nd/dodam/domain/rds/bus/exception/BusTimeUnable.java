package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusTimeUnable extends CustomException {

    public BusTimeUnable() {
        super(BusExceptionCode.BUS_TIME_UNABLE);
    }

}
