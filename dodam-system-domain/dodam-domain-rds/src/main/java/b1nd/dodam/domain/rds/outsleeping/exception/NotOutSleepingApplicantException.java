package b1nd.dodam.domain.rds.outsleeping.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NotOutSleepingApplicantException extends CustomException {

    public NotOutSleepingApplicantException() {
        super(OutSleepingExceptionCode.NOT_APPLICANT);
    }

}
