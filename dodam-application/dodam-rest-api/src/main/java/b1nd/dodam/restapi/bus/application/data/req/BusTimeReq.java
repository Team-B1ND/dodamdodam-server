package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusTime;
import b1nd.dodam.domain.rds.bus.entity.BusTimeToBus;
import jakarta.validation.constraints.NotNull;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;
import java.util.List;

public record BusTimeReq(@NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate startAt,
                         @NotNull @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate endAt,
                         @NotNull List<Integer> busId) {

    public BusTime mapToBusTime(){
        return BusTime.builder()
                .startAt(startAt)
                .endAt(endAt)
                .build();
    }

    public List<BusTimeToBus> mapBusTimeToBus(BusTime busTime, List<Bus> buses) {
        return buses.stream()
                .map(bus -> BusTimeToBus.builder()
                        .bus(bus)
                        .busTime(busTime)
                        .build())
                .toList();
    }

}
