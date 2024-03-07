package b1nd.dodaminfra.webclient.exception;

import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamcore.common.exception.custom.CustomException;

public final class WebClientException extends CustomException {

    public WebClientException(int code) {
        super(
                switch (code) {
                    case 400 -> GlobalExceptionCode.TOKEN_NOT_PROVIDED;
                    case 401 -> GlobalExceptionCode.TOKEN_EXPIRED;
                    case 403 -> GlobalExceptionCode.INVALID_TOKEN;
                    default -> GlobalExceptionCode.INTERNAL_SERVER;
                }
        );
    }

}