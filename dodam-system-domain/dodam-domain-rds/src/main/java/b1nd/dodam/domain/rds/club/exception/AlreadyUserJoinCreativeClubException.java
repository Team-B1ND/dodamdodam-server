package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class AlreadyUserJoinCreativeClubException extends CustomException {
    public AlreadyUserJoinCreativeClubException() {
        super(ClubExceptionCode.ALREADY_USER_JOIN_CREATIVE_CLUB);
    }
}
