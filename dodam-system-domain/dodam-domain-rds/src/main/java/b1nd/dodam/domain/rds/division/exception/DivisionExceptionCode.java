package b1nd.dodam.domain.rds.division.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum DivisionExceptionCode implements ExceptionCode {
    DIVISION_NOT_FOUND(404, "없는 조직"),
    DIVISION_MEMBER_NOT_FOUNT(404, "없는 조직원");

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