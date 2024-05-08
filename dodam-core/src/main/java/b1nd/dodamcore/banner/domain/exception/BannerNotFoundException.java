package b1nd.dodamcore.banner.domain.exception;

import b1nd.dodamcore.common.exception.CustomException;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value = HttpStatus.NOT_FOUND)
public class BannerNotFoundException extends CustomException {

    public BannerNotFoundException() {
        super(BannerExceptionCode.NOT_FOUND);
    }

}