package b1nd.dodamcore.common.exception.custom;

import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.BAD_REQUEST)
public class ParameterNotFoundException extends CustomException {

    public ParameterNotFoundException() {
        super(GlobalExceptionCode.PARAMETER_NOT_FOUND);
    }
}
