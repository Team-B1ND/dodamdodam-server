package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BusMemberNotFoundException extends CustomException {

    public BusMemberNotFoundException() {
        super(BusExceptionCode.BUS_MEMBER_NOT_FOUND);
    }

}
