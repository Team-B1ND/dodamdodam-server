package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class AlreadyClubLeaderException extends CustomException {
    public AlreadyClubLeaderException() {
        super(ClubExceptionCode.ALREADY_CLUB_LEADER);
    }
}
