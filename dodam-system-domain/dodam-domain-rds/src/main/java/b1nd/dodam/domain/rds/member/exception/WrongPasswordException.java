package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class WrongPasswordException extends CustomException {

    public WrongPasswordException() {
        super(MemberExceptionCode.WRONG_PASSWORD);
    }

}
