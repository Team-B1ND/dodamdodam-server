package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class MemberNotActiveException extends CustomException {

    public MemberNotActiveException() {
        super(MemberExceptionCode.MEMBER_NOT_ACTIVE);
    }
}