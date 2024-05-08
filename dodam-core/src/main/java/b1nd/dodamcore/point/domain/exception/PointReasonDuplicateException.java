package b1nd.dodamcore.point.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class PointReasonDuplicateException extends CustomException {

    public PointReasonDuplicateException() {
        super(PointReasonExceptionCode.DUPLICATE);
    }

}
