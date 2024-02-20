package b1nd.dodamcore.outsleeping.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotOutSleepingApplicantException extends CustomException {

    public NotOutSleepingApplicantException() {
        super(OutSleepingExceptionCode.NOT_APPLICANT);
    }

}