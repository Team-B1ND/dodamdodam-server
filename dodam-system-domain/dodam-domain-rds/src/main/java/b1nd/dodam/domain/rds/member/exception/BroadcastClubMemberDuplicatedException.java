package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BroadcastClubMemberDuplicatedException extends CustomException {

    public BroadcastClubMemberDuplicatedException() {
        super(MemberExceptionCode.BROADCAST_CLUB_MEMBER_DUPLICATED);
    }

}
