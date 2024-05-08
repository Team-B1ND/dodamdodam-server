package b1nd.dodamcore.schedule.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class ScheduleNotFoundException extends CustomException {

    public ScheduleNotFoundException() {
        super(ScheduleExceptionCode.NOT_FOUND);
    }

}