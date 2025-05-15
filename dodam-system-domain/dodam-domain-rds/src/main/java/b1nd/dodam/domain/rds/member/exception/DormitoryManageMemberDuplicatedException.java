package b1nd.dodam.domain.rds.member.exception;

import b1nd.dodam.core.exception.CustomException;

public class DormitoryManageMemberDuplicatedException extends CustomException {

    public DormitoryManageMemberDuplicatedException() {
        super(MemberExceptionCode.DORMITORY_MANAGE_MEMBER_DUPLICATED);
    }

}
