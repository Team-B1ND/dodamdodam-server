package b1nd.dodamcore.wakeupsong.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.FORBIDDEN)
public class NotWakeupSongApplicantException extends CustomException {

    public NotWakeupSongApplicantException() {
        super(WakeupSongExceptionCode.NOT_APPLICANT);
    }

}