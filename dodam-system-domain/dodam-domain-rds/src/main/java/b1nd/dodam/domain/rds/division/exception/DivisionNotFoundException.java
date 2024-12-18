package b1nd.dodam.domain.rds.division.exception;

import b1nd.dodam.core.exception.CustomException;

public class DivisionNotFoundException extends CustomException {
    public DivisionNotFoundException() {
        super(DivisionExceptionCode.DIVISION_NOT_FOUND);
    }
}
