package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;
import b1nd.dodam.core.exception.ExceptionCode;

public class BusNotFoundException extends CustomException {
    public BusNotFoundException() {
        super(BusExceptionCode.BUS_NOT_FOUND);
    }
}
