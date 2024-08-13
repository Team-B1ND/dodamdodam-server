package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class TeacherNotFoundException extends CustomException {

    public TeacherNotFoundException() {
        super(MemberExceptionCode.TEACHER_NOT_FOUND);
    }

}
