package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubNotFoundException extends CustomException {
    public ClubNotFoundException() {
        super(ClubExceptionCode.CLUB_NOT_FOUND_EXCEPTION);
    }
}
