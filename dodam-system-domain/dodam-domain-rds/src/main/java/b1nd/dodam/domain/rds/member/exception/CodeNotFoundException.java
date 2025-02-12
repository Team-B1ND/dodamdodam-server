package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class CodeNotFoundException extends CustomException {
    public CodeNotFoundException(){
        super(MemberExceptionCode.CODE_NOT_FOUND);
    }
}
