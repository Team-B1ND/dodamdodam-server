package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BroadcastClubMemberDuplicateException extends CustomException {

    public BroadcastClubMemberDuplicateException() {
        super(MemberExceptionCode.BROADCAST_CLUB_MEMBER_DUPLICATION);
    }

}
