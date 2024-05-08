package b1nd.dodamcore.wakeupsong.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class WakeupSongUrlMalformedException extends CustomException {

    public WakeupSongUrlMalformedException() {
        super(WakeupSongExceptionCode.URL_MALFORMED);
    }

}