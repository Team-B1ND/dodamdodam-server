package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusAlreadyAppliedPositionException extends CustomException {
    public BusAlreadyAppliedPositionException() {
        super(BusExceptionCode.BUS_ALREADY_APPLIED_POSITION);
    }
}