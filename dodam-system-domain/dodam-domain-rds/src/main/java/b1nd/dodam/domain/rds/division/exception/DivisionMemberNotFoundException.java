package b1nd.dodam.domain.rds.division.exception;

import b1nd.dodam.core.exception.CustomException;

public class DivisionMemberNotFoundException extends CustomException {
    public DivisionMemberNotFoundException() {
        super(DivisionExceptionCode.DIVISION_MEMBER_NOT_FOUNT);
    }
}
