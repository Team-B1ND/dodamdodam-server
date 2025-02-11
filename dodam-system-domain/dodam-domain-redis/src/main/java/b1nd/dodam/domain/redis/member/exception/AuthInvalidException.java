package b1nd.dodam.domain.redis.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class AuthInvalidException extends CustomException {
    public AuthInvalidException() {
        super(MemberInfraExceptionCode.AUTH_INVALID);
    }

}
