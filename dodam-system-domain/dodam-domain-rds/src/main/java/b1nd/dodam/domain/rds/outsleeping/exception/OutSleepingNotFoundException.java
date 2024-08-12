package b1nd.dodam.domain.rds.outsleeping.exception;

import b1nd.dodam.core.exception.CustomException;

public final class OutSleepingNotFoundException extends CustomException {

    public OutSleepingNotFoundException() {
        super(OutSleepingExceptionCode.NOT_FOUND);
    }

}
