package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class AuthCodeNotMatchException extends CustomException {
    public AuthCodeNotMatchException() {
        super(MemberExceptionCode.AUTH_CODE_NOT_MATCH);
    }

}
