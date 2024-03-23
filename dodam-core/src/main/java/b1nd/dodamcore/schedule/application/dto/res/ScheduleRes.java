package b1nd.dodamcore.schedule.application.dto.res;

import b1nd.dodamcore.common.enums.SchoolPlace;
import b1nd.dodamcore.common.enums.TargetGrade;
import b1nd.dodamcore.schedule.domain.entity.Schedule;
import b1nd.dodamcore.schedule.domain.enums.ScheduleType;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

public record ScheduleRes(int id, String name, SchoolPlace place, ScheduleType type,
                          List<LocalDate> date, Set<TargetGrade> targetGrades) {
    public static ScheduleRes of(Schedule schedule) {
        return new ScheduleRes(
                schedule.getId(),
                schedule.getName(),
                schedule.getPlace(),
                schedule.getType(),
                List.of(schedule.getStartDate(), schedule.getEndDate()),
                schedule.getTargetGrades()
        );
    }
}