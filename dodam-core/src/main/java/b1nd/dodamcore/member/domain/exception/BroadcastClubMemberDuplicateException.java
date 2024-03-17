package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.CONFLICT)
public class BroadcastClubMemberDuplicateException extends CustomException {

    public BroadcastClubMemberDuplicateException() {
        super(MemberExceptionCode.BROADCAST_CLUB_MEMBER_DUPLICATION);
    }

}