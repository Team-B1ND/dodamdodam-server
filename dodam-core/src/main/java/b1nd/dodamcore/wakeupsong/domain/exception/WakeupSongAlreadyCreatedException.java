package b1nd.dodamcore.wakeupsong.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.LOCKED)
public class WakeupSongAlreadyCreatedException extends CustomException {

    public WakeupSongAlreadyCreatedException() {
        super(WakeupSongExceptionCode.ALREADY_APPLIED);
    }

}