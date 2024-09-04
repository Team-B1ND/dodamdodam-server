package b1nd.dodam.domain.rds.wakeupsong.exception;

import b1nd.dodam.core.exception.CustomException;

public final class UnsupportedVideoTypeException extends CustomException {

    public UnsupportedVideoTypeException() {
        super(WakeupSongExceptionCode.UNSUPPORTED_TYPE);
    }

}