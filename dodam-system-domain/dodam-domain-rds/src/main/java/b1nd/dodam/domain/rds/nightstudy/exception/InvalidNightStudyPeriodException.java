package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class InvalidNightStudyPeriodException extends CustomException {

    public InvalidNightStudyPeriodException() {
        super(NightStudyExceptionCode.INVALID_STUDY_DURATION);
    }

}
