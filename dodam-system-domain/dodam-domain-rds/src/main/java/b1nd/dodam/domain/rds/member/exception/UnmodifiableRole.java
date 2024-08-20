package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class UnmodifiableRole extends CustomException {

    public UnmodifiableRole() {
        super(MemberExceptionCode.UNMODIFIABLE_ROLE);
    }

}

