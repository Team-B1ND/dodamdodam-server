package b1nd.dodam.domain.rds.notice.exception;

import b1nd.dodam.core.exception.CustomException;

public final class NoticeDivisionNotFoundException extends CustomException {

    public NoticeDivisionNotFoundException() {

        super(NoticeExceptionCode.NOT_FOUND);

    }

}
