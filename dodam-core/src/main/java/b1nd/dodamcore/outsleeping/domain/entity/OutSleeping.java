package b1nd.dodamcore.outsleeping.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.outsleeping.domain.enums.OutSleepingStatus;
import b1nd.dodamcore.outsleeping.domain.exception.InvalidOutSleepingPeriodException;
import b1nd.dodamcore.outsleeping.domain.exception.NotOutSleepingApplicantException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutSleeping extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String reason;

    @NotNull
    private LocalDate startAt;

    @NotNull
    private LocalDate endAt;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private OutSleepingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    private String rejectReason;

    @Builder
    public OutSleeping(String reason, LocalDate startAt, LocalDate endAt, Student student) {
        isInvalidPeriod(startAt, endAt);

        this.reason = reason;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = OutSleepingStatus.PENDING;
        this.student = student;
    }

    public void modifyStatus(Teacher teacher, OutSleepingStatus status, String rejectReason) {
        this.status = status;
        this.teacher = teacher;
        this.rejectReason = rejectReason;
    }

    public boolean isNotApplicant(Student student) {
        return this.student.getId() != student.getId();
    }

    private void isInvalidPeriod(LocalDate startAt, LocalDate endAt) {
        if(startAt.isAfter(endAt)) {
            throw new InvalidOutSleepingPeriodException();
        }
    }

}
