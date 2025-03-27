package b1nd.dodam.domain.rds.bus.exception;

import b1nd.dodam.core.exception.CustomException;

public class BusPresetNameDuplicated extends CustomException {

    public BusPresetNameDuplicated() {
        super(BusExceptionCode.BUS_PRESET_NAME_DUPLICATED);
    }

}
