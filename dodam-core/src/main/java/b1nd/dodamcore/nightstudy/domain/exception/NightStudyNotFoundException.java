package b1nd.dodamcore.nightstudy.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class NightStudyNotFoundException extends CustomException {

    public NightStudyNotFoundException() {
        super(NightStudyExceptionCode.NOT_FOUND);
    }

}