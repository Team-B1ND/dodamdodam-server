package b1nd.dodam.domain.rds.wakeupsong.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NotWakeupSongApplicantException extends CustomException {

    public NotWakeupSongApplicantException() {
        super(WakeupSongExceptionCode.NOT_APPLICANT);
    }

}
