package b1nd.dodamcore.wakeupsong.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class WakeupSongNotFoundException extends CustomException {

    public WakeupSongNotFoundException() {
        super(WakeupSongExceptionCode.NOT_FOUND);
    }

}