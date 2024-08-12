package b1nd.dodam.domain.rds.outsleeping.exception;

import b1nd.dodam.core.exception.CustomException;

public final class InvalidOutSleepingPeriodException extends CustomException {

    public InvalidOutSleepingPeriodException() {
        super(OutSleepingExceptionCode.INVALID_PERIOD);
    }

}
