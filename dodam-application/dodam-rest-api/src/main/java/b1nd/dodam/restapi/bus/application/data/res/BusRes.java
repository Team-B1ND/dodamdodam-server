package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.Bus;

import java.util.List;

public record BusRes(Bus bus, List<BusMemberRes> members) {

    public static BusRes createFromBus(Bus bus, List<BusMemberRes> members) {
        return new BusRes(bus, members);
    }
}
