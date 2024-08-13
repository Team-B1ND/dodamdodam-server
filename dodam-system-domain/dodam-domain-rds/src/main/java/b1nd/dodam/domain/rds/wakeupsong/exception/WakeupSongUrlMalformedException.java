package b1nd.dodam.domain.rds.wakeupsong.exception;

import b1nd.dodam.core.exception.CustomException;

public final class WakeupSongUrlMalformedException extends CustomException {

    public WakeupSongUrlMalformedException() {
        super(WakeupSongExceptionCode.URL_MALFORMED);
    }

}
