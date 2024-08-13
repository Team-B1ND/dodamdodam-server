package b1nd.dodam.domain.rds.nightstudy.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NightStudyDuplicateException extends CustomException {

    public NightStudyDuplicateException() {
        super(NightStudyExceptionCode.STUDY_DUPLICATE);
    }

}
