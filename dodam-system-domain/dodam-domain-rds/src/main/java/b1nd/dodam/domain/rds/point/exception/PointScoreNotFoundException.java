package b1nd.dodam.domain.rds.point.exception;

import b1nd.dodam.core.exception.CustomException;

public final class PointScoreNotFoundException extends CustomException {

    public PointScoreNotFoundException() {
        super(PointScoreExceptionCode.NOT_FOUND);
    }

}
