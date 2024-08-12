package b1nd.dodam.domain.rds.support.exception;

import b1nd.dodam.core.exception.CustomException;

public final class SchoolPlaceNotFoundException extends CustomException {

    public SchoolPlaceNotFoundException() {
        super(SchoolPlaceExceptionCode.NOT_FOUND);
    }

}
