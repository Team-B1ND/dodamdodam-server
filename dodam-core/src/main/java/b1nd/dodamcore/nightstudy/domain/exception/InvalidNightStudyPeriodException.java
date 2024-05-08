package b1nd.dodamcore.nightstudy.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class InvalidNightStudyPeriodException extends CustomException {

    public InvalidNightStudyPeriodException() {
        super(NightStudyExceptionCode.INVALID_STUDY_DURATION);
    }

}