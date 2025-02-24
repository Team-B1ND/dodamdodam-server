package b1nd.dodam.domain.rds.club.exception;

import b1nd.dodam.core.exception.CustomException;

public class ClubMemberNotFoundException extends CustomException {
    public ClubMemberNotFoundException() {
        super(ClubExceptionCode.ClUB_MEMBER_NOT_FOUND);
    }
}
