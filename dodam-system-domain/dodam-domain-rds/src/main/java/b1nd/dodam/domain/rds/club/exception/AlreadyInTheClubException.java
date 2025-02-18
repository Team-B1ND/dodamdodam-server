package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class AlreadyInTheClubException extends CustomException {
    public AlreadyInTheClubException() {
        super(ClubExceptionCode.ALREADY_IN_THE_CLUB);
    }
}
