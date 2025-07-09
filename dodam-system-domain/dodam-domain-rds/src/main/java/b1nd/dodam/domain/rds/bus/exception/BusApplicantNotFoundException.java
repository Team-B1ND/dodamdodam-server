package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusApplicantNotFoundException extends CustomException {
    public BusApplicantNotFoundException() {
        super(BusExceptionCode.BUS_APPLICANT_NOT_FOUND);
    }
}
