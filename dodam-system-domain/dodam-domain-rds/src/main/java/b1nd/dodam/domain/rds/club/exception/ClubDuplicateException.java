package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubDuplicateException extends CustomException {
    public ClubDuplicateException() {
        super(ClubExceptionCode.CLUB_NAME_DUPLICATE);
    }
}
