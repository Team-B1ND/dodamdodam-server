package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class MemberNotFoundException extends CustomException {

    public MemberNotFoundException() {
        super(MemberExceptionCode.MEMBER_NOT_FOUND);
    }

}