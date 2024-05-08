package b1nd.dodamcore.recruit.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class RecruitNotFoundException extends CustomException {

    public RecruitNotFoundException() {
        super(RecruitExceptionCode.NOT_FOUND);
    }

}