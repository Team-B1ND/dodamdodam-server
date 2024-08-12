package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class StudentNotFoundException extends CustomException {

    public StudentNotFoundException() {
        super(MemberExceptionCode.STUDENT_NOT_FOUND);
    }

}
