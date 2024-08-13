package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NightStudyApplicationDurationPassedException extends CustomException {

    public NightStudyApplicationDurationPassedException() {
        super(NightStudyExceptionCode.APPLICATION_DURATION_PASSED);
    }

}
