package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class UnauthorizedException extends CustomException {

    public UnauthorizedException() {
        super(MemberExceptionCode.UNAUTHORIZED);
    }

}

