package b1nd.dodam.domain.rds.division.exception;

import b1nd.dodam.core.exception.CustomException;


public class DivisionMemberDuplicateException extends CustomException {
    public DivisionMemberDuplicateException() {
        super(DivisionExceptionCode.DIVISION_MEMBER_DUPLICATE);
    }
}
