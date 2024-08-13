package b1nd.dodam.domain.rds.schedule.exception;

import b1nd.dodam.core.exception.CustomException;

public final class ScheduleNotFoundException extends CustomException {

    public ScheduleNotFoundException() {
        super(ScheduleExceptionCode.NOT_FOUND);
    }

}
