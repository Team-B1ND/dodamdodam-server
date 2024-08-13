package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDateTime;
import java.time.LocalTime;

public record BusReq(@NotEmpty String busName, @NotEmpty String description,
                           @NotNull LocalDateTime leaveTime, @NotNull LocalTime timeRequired, @NotNull int peopleLimit) {
    public Bus mapToBus() {
        return Bus.builder()
                .busName(busName)
                .description(description)
                .peopleLimit(peopleLimit)
                .leaveTime(leaveTime)
                .timeRequired(timeRequired)
                .build();
    }
}
