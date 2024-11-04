package b1nd.dodam.domain.rds.schedule.entity;

import b1nd.dodam.domain.rds.schedule.enumeration.ScheduleType;
import b1nd.dodam.domain.rds.schedule.enumeration.TargetGrade;
import b1nd.dodam.domain.rds.support.enumeration.SchoolPlace;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.DynamicUpdate;

import java.time.LocalDate;
import java.util.Objects;
import java.util.Set;

@Getter
@Entity(name = "schedule")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@DynamicUpdate
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    private String name;

    @Enumerated(value = EnumType.STRING)
    private SchoolPlace place;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ScheduleType type;

    @NotNull
    private LocalDate startDate;

    @NotNull
    private LocalDate endDate;

    @ElementCollection(targetClass = TargetGrade.class)
    @CollectionTable(
            name = "schedule_of_grade",
            joinColumns = @JoinColumn(name = "schedule_id")
    )

    @Enumerated(EnumType.STRING)
    private Set<TargetGrade> targetGrades;

    @Builder
    public Schedule(String name, String place, LocalDate startDate, LocalDate endDate, Set<TargetGrade> targetGrades) {
        this.name = name;
        this.place = (place == null) ? null : SchoolPlace.of(place);
        this.startDate = startDate;
        this.endDate = endDate;
        this.targetGrades = targetGrades;
        this.type = (this.place == null) ? ScheduleType.HOLIDAY : ScheduleType.ACADEMIC;
    }

    public void updateSchedule(String name, String place, LocalDate startDate, LocalDate endDate, Set<TargetGrade> targetGrades) {
        if(Objects.nonNull(name)) {
            this.name = name;
        }
        if(Objects.nonNull(place)) {
            this.place = SchoolPlace.of(place);
        }
        if(Objects.nonNull(startDate)) {
            this.startDate = startDate;
        }
        if(Objects.nonNull(endDate)) {
            this.endDate = endDate;
        }
        if(Objects.nonNull(targetGrades)) {
            this.targetGrades = targetGrades;
        }
    }

}
