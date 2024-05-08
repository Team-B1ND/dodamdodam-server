package b1nd.dodamcore.outgoing.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotOutGoingApplicantException extends CustomException {

    public NotOutGoingApplicantException() {
        super(OutGoingExceptionCode.NOT_APPLICANT);
    }

}