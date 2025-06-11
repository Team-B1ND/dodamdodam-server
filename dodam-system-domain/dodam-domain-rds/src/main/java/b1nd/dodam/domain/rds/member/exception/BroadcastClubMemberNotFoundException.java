package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BroadcastClubMemberNotFoundException extends CustomException {
    public BroadcastClubMemberNotFoundException() {
        super(MemberExceptionCode.BROADCAST_CLUB_MEMBER_NOT_FOUND);
    }
}
