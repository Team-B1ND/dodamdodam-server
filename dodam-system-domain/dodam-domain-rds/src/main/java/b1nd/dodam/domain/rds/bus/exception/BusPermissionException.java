package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusPermissionException extends CustomException {

    public BusPermissionException() {
        super(BusExceptionCode.BUS_ACCESS_DENIED);
    }

}
