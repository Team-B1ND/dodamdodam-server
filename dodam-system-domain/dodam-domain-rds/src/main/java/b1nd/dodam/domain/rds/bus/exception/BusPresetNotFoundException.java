package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusPresetNotFoundException extends CustomException {

    public BusPresetNotFoundException() {
        super(BusExceptionCode.BUS_PRESET_NOT_FOUND);
    }

}
