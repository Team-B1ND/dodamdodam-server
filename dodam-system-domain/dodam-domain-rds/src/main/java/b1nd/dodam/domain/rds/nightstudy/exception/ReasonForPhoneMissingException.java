package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class ReasonForPhoneMissingException extends CustomException {

    public ReasonForPhoneMissingException() {
        super(NightStudyExceptionCode.REASON_FOR_PHONE_MISSING);
    }

}
