package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubApplicationNotAllowedException extends CustomException {
    public ClubApplicationNotAllowedException() {
        super(ClubExceptionCode.CLUB_APPLICATION_NOT_ALLOWED);
    }
}
