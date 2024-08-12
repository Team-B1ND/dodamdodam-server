package b1nd.dodam.domain.rds.recruitment.exception;

import b1nd.dodam.core.exception.CustomException;

public final class RecruitNotFoundException extends CustomException {

    public RecruitNotFoundException() {
        super(RecruitExceptionCode.NOT_FOUND);
    }

}
