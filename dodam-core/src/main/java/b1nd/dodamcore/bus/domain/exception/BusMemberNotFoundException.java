package b1nd.dodamcore.bus.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BusMemberNotFoundException extends CustomException {

    public BusMemberNotFoundException() {
        super(BusExceptionCode.BUS_MEMBER_NOT_FOUND);
    }

}