package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NightStudyNotFoundException extends CustomException {

    public NightStudyNotFoundException() {
        super(NightStudyExceptionCode.NOT_FOUND);
    }

}
