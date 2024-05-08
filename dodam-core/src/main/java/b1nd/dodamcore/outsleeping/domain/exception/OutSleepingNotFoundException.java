package b1nd.dodamcore.outsleeping.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OutSleepingNotFoundException extends CustomException {

    public OutSleepingNotFoundException() {
        super(OutSleepingExceptionCode.NOT_FOUND);
    }

}