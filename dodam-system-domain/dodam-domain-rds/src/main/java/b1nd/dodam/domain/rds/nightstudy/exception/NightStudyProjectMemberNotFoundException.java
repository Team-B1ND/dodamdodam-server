package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public class NightStudyProjectMemberNotFoundException extends CustomException {
    public NightStudyProjectMemberNotFoundException() {
        super(NightStudyExceptionCode.MEMBER_NOT_FOUND);
    }
}
