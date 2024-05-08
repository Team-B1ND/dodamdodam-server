package b1nd.dodamcore.nightstudy.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NightStudyDuplicateException extends CustomException {

    public NightStudyDuplicateException() {
        super(NightStudyExceptionCode.STUDY_DUPLICATE);
    }

}