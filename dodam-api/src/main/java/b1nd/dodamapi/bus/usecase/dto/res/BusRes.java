package b1nd.dodamapi.bus.usecase.dto.res;

import b1nd.dodamcore.bus.domain.entity.Bus;

import java.util.List;

public record BusRes(Bus bus, List<BusMemberRes> members) {

    public static BusRes createFromBus(Bus bus, List<BusMemberRes> members) {
        return new BusRes(bus, members);
    }
}
