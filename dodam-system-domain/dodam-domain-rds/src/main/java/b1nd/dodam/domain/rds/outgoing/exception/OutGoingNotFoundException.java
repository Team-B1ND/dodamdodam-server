package b1nd.dodam.domain.rds.outgoing.exception;

import b1nd.dodam.core.exception.CustomException;

public final class OutGoingNotFoundException extends CustomException {

    public OutGoingNotFoundException() {
        super(OutGoingExceptionCode.NOT_FOUND);
    }

}
