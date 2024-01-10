package b1nd.dodamcore.member.domain.exception;

import b1nd.dodamcore.common.exception.custom.CustomException;

public class MemberNotFoundException extends CustomException {

    public MemberNotFoundException() {
        super(MemberExceptionCode.MEMBER_NOT_FOUND);
    }

}