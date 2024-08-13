package b1nd.dodam.domain.rds.point.exception;

import b1nd.dodam.core.exception.CustomException;

public final class PointReasonNotFoundException extends CustomException {

    public PointReasonNotFoundException() {
        super(PointReasonExceptionCode.NOT_FOUND);
    }

}
