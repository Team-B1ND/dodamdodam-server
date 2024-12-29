package b1nd.dodam.domain.rds.notice.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NoticeNotFoundException extends CustomException {
    public NoticeNotFoundException() {
        super(NoticeExceptionCode.NOT_FOUND);
    }

}
