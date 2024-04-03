package b1nd.dodamcore.point.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PointReasonNotFoundException extends CustomException {

    public PointReasonNotFoundException() {
        super(PointReasonExceptionCode.NOT_FOUND);
    }

}
