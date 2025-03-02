package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.BusApplication;

import java.util.List;

public record BusSeatRes(
        List<Integer> busSeat
) {
    public static BusSeatRes of(List<BusApplication> applications) {
        return new BusSeatRes(applications.stream()
                .map(BusApplication::getSeatNumber)
                .toList());
    }
}
