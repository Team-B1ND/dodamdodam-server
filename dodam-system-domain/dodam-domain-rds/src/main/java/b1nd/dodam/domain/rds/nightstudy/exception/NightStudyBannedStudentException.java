package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NightStudyBannedStudentException extends CustomException {

    public NightStudyBannedStudentException() {
        super(NightStudyExceptionCode.BANNED_STUDENT);
    }
}
