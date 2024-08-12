package b1nd.dodam.client.core.exception;

import b1nd.dodam.core.exception.CustomException;
import b1nd.dodam.core.exception.global.GlobalExceptionCode;

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
