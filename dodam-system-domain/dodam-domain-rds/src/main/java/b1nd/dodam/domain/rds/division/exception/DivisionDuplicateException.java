package b1nd.dodam.domain.rds.division.exception;

import b1nd.dodam.core.exception.CustomException;


public class DivisionDuplicateException extends CustomException {
    public DivisionDuplicateException() {
        super(DivisionExceptionCode.DIVISION_NAME_DUPLICATE);
    }
}
