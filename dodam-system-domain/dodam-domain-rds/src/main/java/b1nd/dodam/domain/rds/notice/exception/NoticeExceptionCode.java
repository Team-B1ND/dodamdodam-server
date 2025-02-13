package b1nd.dodam.domain.rds.notice.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum NoticeExceptionCode implements ExceptionCode {

    NOT_APPLICANT(403, "작성자가 아님"),
    NOT_FOUND(404, "찾을 수 없음");

    private final int status;
    private final String message;

    @Override
    public int getStatus() {
        return this.status;
    }

    @Override
    public String getExceptionName() {
        return this.name();
    }

    @Override
    public String getMessage() {
        return this.message;
    }

}
