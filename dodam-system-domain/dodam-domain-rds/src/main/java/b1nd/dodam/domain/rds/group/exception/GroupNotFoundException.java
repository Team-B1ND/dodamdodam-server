package b1nd.dodam.domain.rds.group.exception;

import b1nd.dodam.core.exception.CustomException;

public class GroupNotFoundException extends CustomException {
    public GroupNotFoundException() {
        super(GroupExceptionCode.GROUP_NOT_FOUND);
    }
}
