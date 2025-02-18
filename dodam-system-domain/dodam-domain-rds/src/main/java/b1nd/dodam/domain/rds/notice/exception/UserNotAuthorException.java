package b1nd.dodam.domain.rds.notice.exception;

import b1nd.dodam.core.exception.CustomException;

public class UserNotAuthorException extends CustomException {

    public UserNotAuthorException() {
        super(NoticeExceptionCode.NOT_AUTHOR);
    }

}
