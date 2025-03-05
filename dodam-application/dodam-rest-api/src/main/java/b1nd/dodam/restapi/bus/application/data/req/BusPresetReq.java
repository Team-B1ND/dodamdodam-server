package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.entity.BusPreset;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalTime;

public record BusPresetReq(@NotEmpty String busName, @NotEmpty String description,
                           @NotNull int peopleLimit, @NotNull LocalTime leaveTime, @NotNull LocalTime timeRequired) {

    public BusPreset mapToBusPreset(){
        return BusPreset.builder()
                .name(busName)
                .description(description)
                .peopleLimit(peopleLimit)
                .leaveTime(leaveTime)
                .timeRequired(timeRequired)
                .build();
    }

}
