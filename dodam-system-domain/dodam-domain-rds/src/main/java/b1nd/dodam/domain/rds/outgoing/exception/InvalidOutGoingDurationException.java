package b1nd.dodam.domain.rds.outgoing.exception;

import b1nd.dodam.core.exception.CustomException;

public final class InvalidOutGoingDurationException extends CustomException {

    public InvalidOutGoingDurationException() {
        super(OutGoingExceptionCode.INVALID_DURATION);
    }

}
