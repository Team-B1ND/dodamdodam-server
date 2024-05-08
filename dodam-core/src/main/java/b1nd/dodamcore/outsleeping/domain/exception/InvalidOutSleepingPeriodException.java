package b1nd.dodamcore.outsleeping.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidOutSleepingPeriodException extends CustomException {

    public InvalidOutSleepingPeriodException() {
        super(OutSleepingExceptionCode.INVALID_PERIOD);
    }

}