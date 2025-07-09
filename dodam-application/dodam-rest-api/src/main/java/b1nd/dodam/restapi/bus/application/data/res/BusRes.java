package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.Bus;

public record BusRes(
    long id,
    String name
) {
    public static BusRes of(Bus bus) {
        return new BusRes(bus.getId(), bus.getName());
    }
}
