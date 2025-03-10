package b1nd.dodam.restapi.bus.application.data.res;

import b1nd.dodam.domain.rds.bus.entity.BusPreset;

import java.time.LocalTime;

public record BusPresetRes(int id, String name, String description, int peopleLimit, LocalTime leaveTime, LocalTime timeRequired) {

    public static BusPresetRes of(BusPreset busPreset) {
        return new BusPresetRes(
                busPreset.getId(),
                busPreset.getName(),
                busPreset.getDescription(),
                busPreset.getPeopleLimit(),
                busPreset.getLeaveTime(),
                busPreset.getTimeRequired()
        );
    }

}
