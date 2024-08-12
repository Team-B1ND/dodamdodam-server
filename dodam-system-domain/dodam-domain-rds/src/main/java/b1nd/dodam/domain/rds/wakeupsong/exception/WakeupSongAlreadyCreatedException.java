package b1nd.dodam.domain.rds.wakeupsong.exception;

import b1nd.dodam.core.exception.CustomException;

public final class WakeupSongAlreadyCreatedException extends CustomException {

    public WakeupSongAlreadyCreatedException() {
        super(WakeupSongExceptionCode.ALREADY_APPLIED);
    }

}
