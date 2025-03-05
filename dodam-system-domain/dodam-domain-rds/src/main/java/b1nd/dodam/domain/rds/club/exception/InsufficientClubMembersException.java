package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class InsufficientClubMembersException extends CustomException {
    public InsufficientClubMembersException() {
        super(ClubExceptionCode.INSUFFICIENT_CLUB_MEMBERS);
    }
}
