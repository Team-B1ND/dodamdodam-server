package b1nd.dodam.domain.rds.group.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum GroupExceptionCode implements ExceptionCode {
    GROUP_NOT_FOUND(404, "없는 그룹"),
    GROUP_MEMBER_NOT_FOUNT(404, "없는 그룹원");

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
