package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class MemberDuplicateException extends CustomException {

    public MemberDuplicateException() {
        super(MemberExceptionCode.MEMBER_DUPLICATION);
    }
}
