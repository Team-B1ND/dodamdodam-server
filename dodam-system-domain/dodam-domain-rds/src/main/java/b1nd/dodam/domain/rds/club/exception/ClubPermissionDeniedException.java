package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubPermissionDeniedException extends CustomException {
    public ClubPermissionDeniedException() {
        super(ClubExceptionCode.CLUB_PERMISSION_DENIED);
    }
}
