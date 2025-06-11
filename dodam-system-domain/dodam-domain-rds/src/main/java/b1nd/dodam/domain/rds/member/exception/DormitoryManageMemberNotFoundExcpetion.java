package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class DormitoryManageMemberNotFoundExcpetion extends CustomException {
    public DormitoryManageMemberNotFoundExcpetion() {
        super(MemberExceptionCode.DORMITORY_MANAGE_MEMBER_NOT_FOUND);
    }
}
