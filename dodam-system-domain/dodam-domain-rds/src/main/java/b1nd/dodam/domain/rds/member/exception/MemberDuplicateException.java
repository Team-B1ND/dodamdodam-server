package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class MemberDuplicateException extends CustomException {

    public MemberDuplicateException() {
        super(MemberExceptionCode.MEMBER_DUPLICATION);
    }

}
