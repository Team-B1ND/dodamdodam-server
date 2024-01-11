package b1nd.dodamcore.bus.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class BusFullOfSeatException extends CustomException {

    public BusFullOfSeatException() {
        super(BusExceptionCode.BUS_FULL_OF_SEAT);
    }

}