package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class OverflowMemberSizeException extends CustomException {
    public OverflowMemberSizeException() {
        super(ClubExceptionCode.OVERFLOW_MEMBER_SIZE);
    }
}
