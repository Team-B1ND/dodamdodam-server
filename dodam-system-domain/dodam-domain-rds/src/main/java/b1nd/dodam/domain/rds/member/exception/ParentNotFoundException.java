package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class ParentNotFoundException extends CustomException {

    public ParentNotFoundException() {
        super(MemberExceptionCode.PARENT_NOT_FOUND);
    }

}
