package b1nd.dodamcore.point.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class PointScoreNotFoundException extends CustomException {

    public PointScoreNotFoundException() {
        super(PointScoreExceptionCode.NOT_FOUND);
    }

}
