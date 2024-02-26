package b1nd.dodamcore.schedule.application.dto.req;

import b1nd.dodamcore.common.enums.SchoolPlace;
import b1nd.dodamcore.common.enums.TargetGrade;
import b1nd.dodamcore.schedule.domain.entity.Schedule;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;

import java.time.LocalDate;
import java.util.Set;
import java.util.stream.Collectors;

public record ScheduleReq(String place, @NotEmpty String name, @NotNull LocalDate startDate,
                          @NotNull LocalDate endDate, Set<String> grades) {

    public Schedule mapToSchedule() {
        return Schedule.builder()
                .name(name)
                .place(place)
                .startDate(startDate)
                .endDate(endDate)
                .targetGrades(grades.stream().map(TargetGrade::of).collect(Collectors.toSet()))
                .build();
    }
}
