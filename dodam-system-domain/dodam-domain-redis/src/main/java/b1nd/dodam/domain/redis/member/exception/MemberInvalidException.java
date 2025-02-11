package b1nd.dodam.domain.redis.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class MemberInvalidException extends CustomException {
    public MemberInvalidException() {
        super(MemberInfraExceptionCode.MEMBER_INVALID);
    }

}
