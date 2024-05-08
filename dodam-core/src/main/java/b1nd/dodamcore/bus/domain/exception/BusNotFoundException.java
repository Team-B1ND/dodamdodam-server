package b1nd.dodamcore.bus.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusNotFoundException extends CustomException {

    public BusNotFoundException() {
        super(BusExceptionCode.BUS_NOT_FOUND);
    }

}