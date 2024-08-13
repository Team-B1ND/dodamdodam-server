package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusPeriodExpiredException extends CustomException {

    public BusPeriodExpiredException() {
        super(BusExceptionCode.BUS_PERIOD_EXPIRED);
    }

}
