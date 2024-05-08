package b1nd.dodamcore.bus.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BusAlreadyApplyException extends CustomException {

    public BusAlreadyApplyException() {
        super(BusExceptionCode.BUS_ALREADY_APPLIED);
    }

}