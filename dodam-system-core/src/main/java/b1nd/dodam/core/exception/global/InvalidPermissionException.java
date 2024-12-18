package b1nd.dodam.core.exception.global;

import b1nd.dodam.core.exception.CustomException;

public final class InvalidPermissionException extends CustomException {

    public InvalidPermissionException() {
        super(GlobalExceptionCode.INVALID_PERMISSION);
    }

}
