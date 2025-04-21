package b1nd.dodam.domain.rds.nightstudy.entity;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.nightstudy.exception.InvalidNightStudyPeriodException;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyApplicationDurationPassedException;
import b1nd.dodam.domain.rds.nightstudy.exception.ReasonForPhoneMissingException;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NightStudy extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Size(min = 10, max = 250)
    private String content;

    @NotNull
    private Boolean doNeedPhone;

    private String reasonForPhone;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    private String rejectReason;

    @NotNull
    private LocalDate startAt;

    @NotNull
    private LocalDate endAt;

    @Builder
    public NightStudy(String content, boolean doNeedPhone, String reasonForPhone, Student student,
                      LocalDate startAt, LocalDate endAt) {
        isApplicationDuration();
        isInvalidStudyPeriod(startAt, endAt);
        doesHaveReasonForPhone(doNeedPhone, reasonForPhone);

        this.content = content;
        this.doNeedPhone = doNeedPhone;
        this.reasonForPhone = reasonForPhone;
        this.status = ApprovalStatus.PENDING;
        this.student = student;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void reject() {
        this.status = ApprovalStatus.REJECTED;
    }

    public void modifyStatus(Teacher teacher, ApprovalStatus status, String rejectReason) {
        this.status = status;
        this.teacher = teacher;
        this.rejectReason = rejectReason;
    }

    public boolean isApplicant(Student student) {
        return this.student.getId() != student.getId();
    }

    private void isApplicationDuration() {
        if(ZonedDateTimeUtil.nowToLocalTime().isAfter(LocalTime.of(20, 30))) {
            throw new NightStudyApplicationDurationPassedException();
        }
    }

    private void isInvalidStudyPeriod(LocalDate startAt, LocalDate endAt) {
        if(startAt.isAfter(endAt) || startAt.plusDays(13L).isBefore(endAt)) {
            throw new InvalidNightStudyPeriodException();
        }
    }

    private void doesHaveReasonForPhone(boolean doNeedPhone, String reasonForPhone) {
        if(doNeedPhone && reasonForPhone == null) {
            throw new ReasonForPhoneMissingException();
        }
    }
}
