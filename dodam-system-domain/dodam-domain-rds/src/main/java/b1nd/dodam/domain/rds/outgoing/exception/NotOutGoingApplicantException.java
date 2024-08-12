package b1nd.dodam.domain.rds.outgoing.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NotOutGoingApplicantException extends CustomException {

    public NotOutGoingApplicantException() {
        super(OutGoingExceptionCode.NOT_APPLICANT);
    }

}
