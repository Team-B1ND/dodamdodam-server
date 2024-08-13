package b1nd.dodam.core.exception.global;

import b1nd.dodam.core.exception.CustomException;

public final class InternalServerException extends CustomException {

    public InternalServerException() {
        super(GlobalExceptionCode.INTERNAL_SERVER);
    }

}
