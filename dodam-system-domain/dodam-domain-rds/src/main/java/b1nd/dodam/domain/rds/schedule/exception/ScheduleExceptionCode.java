package b1nd.dodam.domain.rds.schedule.exception;

import b1nd.dodam.core.exception.ExceptionCode;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public enum ScheduleExceptionCode implements ExceptionCode {

    NOT_FOUND(404, "없는 일정");

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
