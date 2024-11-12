package b1nd.dodam.domain.rds.group.exception;

import b1nd.dodam.core.exception.CustomException;

public class GroupMemberNotFoundException extends CustomException {
    public GroupMemberNotFoundException() {
        super(GroupExceptionCode.GROUP_MEMBER_NOT_FOUNT);
    }
}
