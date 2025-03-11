package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalTime;

public record BusReq(@NotEmpty String busName, @NotEmpty String description, @NotNull LocalDate leaveAt,
                     @NotNull LocalTime leaveTime, @NotNull LocalTime timeRequired, @NotNull int peopleLimit) {
    public Bus mapToBus() {
        return Bus.builder()
                .busName(busName)
                .description(description)
                .status(BusStatus.ACTIVATE)
                .peopleLimit(peopleLimit)
                .leaveAt(leaveAt)
                .leaveTime(leaveTime)
                .timeRequired(timeRequired)
                .build();
    }
}
