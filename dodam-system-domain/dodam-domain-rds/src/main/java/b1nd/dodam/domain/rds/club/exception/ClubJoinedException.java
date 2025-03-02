package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubJoinedException extends CustomException {
    public ClubJoinedException() {
        super(ClubExceptionCode.CLUB_JOINED);
    }
}
