package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class DeactivateMemberException extends CustomException {

    public DeactivateMemberException() {
        super(MemberExceptionCode.DEACTIVATE_MEMBER);
    }

}
