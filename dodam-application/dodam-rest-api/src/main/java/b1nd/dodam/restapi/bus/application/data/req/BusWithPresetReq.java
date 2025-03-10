package b1nd.dodam.restapi.bus.application.data.req;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusPreset;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

public record BusWithPresetReq(@NotNull LocalDate leaveTime) {

    public Bus mapToBus(BusPreset busPreset, LocalDate leaveDate) {
        return Bus.builder()
                .busName(busPreset.getName())
                .description(busPreset.getDescription())
                .status(BusStatus.ACTIVATE)
                .peopleLimit(busPreset.getPeopleLimit())
                .leaveTime(leaveDate.atTime(busPreset.getLeaveTime()))
                .timeRequired(busPreset.getTimeRequired())
                .build();
    }

}
