package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class TeacherNotFoundException extends CustomException {

    public TeacherNotFoundException() {
        super(MemberExceptionCode.TEACHER_NOT_FOUND);
    }

}