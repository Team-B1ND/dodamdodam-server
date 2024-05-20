package b1nd.dodamcore.wakeupsong.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.UNPROCESSABLE_ENTITY)
public class UnsupportedVideoTypeException extends CustomException {

    public UnsupportedVideoTypeException() {
        super(WakeupSongExceptionCode.UNSUPPORTED_TYPE);
    }

}