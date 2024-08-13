package b1nd.dodam.domain.rds.wakeupsong.exception;

import b1nd.dodam.core.exception.CustomException;

public final class WakeupSongNotFoundException extends CustomException {

    public WakeupSongNotFoundException() {
        super(WakeupSongExceptionCode.NOT_FOUND);
    }

}
