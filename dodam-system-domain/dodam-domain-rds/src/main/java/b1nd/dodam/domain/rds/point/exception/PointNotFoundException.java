package b1nd.dodam.domain.rds.point.exception;

import b1nd.dodam.core.exception.CustomException;

public final class PointNotFoundException extends CustomException {

    public PointNotFoundException() {
        super(PointExceptionCode.NOT_FOUND);
    }

}
