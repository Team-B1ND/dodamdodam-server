package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class InvalidClubMemberInviteException extends CustomException {
    public InvalidClubMemberInviteException() {
        super(ClubExceptionCode.INVALID_CLUB_MEMBER_INVITATION);
    }
}
