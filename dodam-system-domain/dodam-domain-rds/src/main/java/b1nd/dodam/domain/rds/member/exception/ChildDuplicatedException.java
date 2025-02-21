package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class ChildDuplicatedException extends CustomException {
    public ChildDuplicatedException(){
        super(MemberExceptionCode.CHILD_DUPLICATED);
    }
}
