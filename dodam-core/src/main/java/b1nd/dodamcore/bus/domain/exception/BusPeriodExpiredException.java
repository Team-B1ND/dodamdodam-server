package b1nd.dodamcore.bus.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.GONE)
public class BusPeriodExpiredException extends CustomException {

    public BusPeriodExpiredException() {
        super(BusExceptionCode.BUS_PERIOD_EXPIRED);
    }

}