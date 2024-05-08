package b1nd.dodaminfra.webclient.exception;

import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamcore.common.exception.CustomException;

public final class WebClientException extends CustomException {

    public WebClientException(int code) {
        super(
                switch (code) {
                    case 400 -> GlobalExceptionCode.TOKEN_NOT_PROVIDED;
                    case 401, 500 -> GlobalExceptionCode.INVALID_TOKEN;
                    case 410 -> GlobalExceptionCode.TOKEN_EXPIRED;
                    default -> GlobalExceptionCode.INTERNAL_SERVER;
                }
        );
    }

}
