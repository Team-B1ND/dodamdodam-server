package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubApplicationDurationPassedException extends CustomException {
    public ClubApplicationDurationPassedException() {
        super(ClubExceptionCode.APPLICATION_DURATION_PASSED);
    }
}
