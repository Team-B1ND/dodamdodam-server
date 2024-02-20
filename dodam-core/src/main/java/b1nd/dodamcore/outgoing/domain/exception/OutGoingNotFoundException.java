package b1nd.dodamcore.outgoing.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class OutGoingNotFoundException extends CustomException {

    public OutGoingNotFoundException() {
        super(OutGoingExceptionCode.NOT_FOUND);
    }

}