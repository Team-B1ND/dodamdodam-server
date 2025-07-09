package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.entity.Bus;

public record BusReq(
    String name
) {
    public Bus toEntity() {
        return Bus.builder().name(name).build();
    }
}
