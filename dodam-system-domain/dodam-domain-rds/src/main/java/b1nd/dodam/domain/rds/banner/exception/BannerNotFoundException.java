package b1nd.dodam.domain.rds.banner.exception;

import b1nd.dodam.core.exception.CustomException;

public final class BannerNotFoundException extends CustomException {

    public BannerNotFoundException() {
        super(BannerExceptionCode.NOT_FOUND);
    }

}
