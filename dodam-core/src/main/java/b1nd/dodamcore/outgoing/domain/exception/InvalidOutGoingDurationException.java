package b1nd.dodamcore.outgoing.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidOutGoingDurationException extends CustomException {

    public InvalidOutGoingDurationException() {
        super(OutGoingExceptionCode.INVALID_DURATION);
    }

}