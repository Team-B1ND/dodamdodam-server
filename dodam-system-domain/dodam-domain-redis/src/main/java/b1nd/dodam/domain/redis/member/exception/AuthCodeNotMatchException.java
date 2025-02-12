package b1nd.dodam.domain.redis.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class AuthCodeNotMatchException extends CustomException {
    public AuthCodeNotMatchException() {
        super(MemberRedisExceptionCode.AUTH_CODE_NOT_MATCH);
    }

}
