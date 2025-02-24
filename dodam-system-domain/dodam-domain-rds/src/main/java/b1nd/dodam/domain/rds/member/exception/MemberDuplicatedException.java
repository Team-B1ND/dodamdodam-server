package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class MemberDuplicatedException extends CustomException {

    public MemberDuplicatedException() {
        super(MemberExceptionCode.MEMBER_DUPLICATED);
    }

}
