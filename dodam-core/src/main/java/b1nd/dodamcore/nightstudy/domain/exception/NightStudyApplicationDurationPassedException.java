package b1nd.dodamcore.nightstudy.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NightStudyApplicationDurationPassedException extends CustomException {

    public NightStudyApplicationDurationPassedException() {
        super(NightStudyExceptionCode.APPLICATION_DURATION_PASSED);
    }

}