package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class AlreadyClubOwnerException extends CustomException {
    public AlreadyClubOwnerException() {
        super(ClubExceptionCode.ALREADY_CLUB_OWNER);
    }
}
