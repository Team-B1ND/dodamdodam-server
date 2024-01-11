package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class StudentNotFoundException extends CustomException {

    public StudentNotFoundException() {
        super(MemberExceptionCode.STUDENT_NOT_FOUND);
    }

}