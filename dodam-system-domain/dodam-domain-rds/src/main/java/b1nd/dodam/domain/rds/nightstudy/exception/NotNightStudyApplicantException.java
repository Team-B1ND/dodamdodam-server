package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NotNightStudyApplicantException extends CustomException {

    public NotNightStudyApplicantException() {
        super(NightStudyExceptionCode.NOT_APPLICANT);
    }

}
