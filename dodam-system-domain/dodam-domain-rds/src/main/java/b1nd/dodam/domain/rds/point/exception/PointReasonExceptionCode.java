package b1nd.dodam.domain.rds.point.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum PointReasonExceptionCode implements ExceptionCode {

    NOT_FOUND(404, "없는 상벌점 사유"),
    DUPLICATE(409, "중복된 상벌점 사유");

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
