package b1nd.dodam.domain.rds.point.exception;

import b1nd.dodam.core.exception.CustomException;

public final class PointReasonDuplicateException extends CustomException {

    public PointReasonDuplicateException() {
        super(PointReasonExceptionCode.DUPLICATE);
    }

}
