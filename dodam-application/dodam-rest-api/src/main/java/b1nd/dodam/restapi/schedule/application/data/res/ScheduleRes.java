package b1nd.dodam.restapi.schedule.application.data.res;

import b1nd.dodam.domain.rds.schedule.entity.Schedule;
import b1nd.dodam.domain.rds.schedule.enumeration.ScheduleType;
import b1nd.dodam.domain.rds.schedule.enumeration.TargetGrade;
import b1nd.dodam.domain.rds.support.enumeration.SchoolPlace;

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

    public static List<ScheduleRes> of(List<Schedule> schedules) {
        return schedules.stream()
                .map(ScheduleRes::of)
                .toList();
    }
}
