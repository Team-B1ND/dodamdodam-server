package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class ActiveMemberException extends CustomException {

    public ActiveMemberException() {
        super(MemberExceptionCode.ACTIVE_MEMBER);
    }

}
