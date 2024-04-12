package b1nd.dodamcore.nightstudy.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.common.enums.SchoolPlace;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import b1nd.dodamcore.nightstudy.domain.exception.InvalidNightStudyPeriodException;
import b1nd.dodamcore.nightstudy.domain.exception.NightStudyApplicationDurationPassedException;
import b1nd.dodamcore.nightstudy.domain.exception.ReasonForPhoneMissingException;
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
    private SchoolPlace place;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private NightStudyStatus status;

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
    public NightStudy(String content, boolean doNeedPhone, String reasonForPhone, SchoolPlace place, Student student,
                      LocalDate startAt, LocalDate endAt) {
        isApplicationDuration();
        isInvalidStudyPeriod(startAt, endAt);
        doesHaveReasonForPhone(doNeedPhone, reasonForPhone);

        this.content = content;
        this.doNeedPhone = doNeedPhone;
        this.reasonForPhone = reasonForPhone;
        this.place = place;
        this.status = NightStudyStatus.PENDING;
        this.student = student;
        this.startAt = startAt;
        this.endAt = endAt;
    }

    public void modifyStatus(Teacher teacher, NightStudyStatus status, String rejectReason) {
        this.status = status;
        this.teacher = teacher;
        this.rejectReason = rejectReason;
    }

    public boolean isApplicant(Student student) {
        return this.student.getId() != student.getId();
    }

    private void isApplicationDuration() {
        if(ZonedDateTimeUtil.nowToLocalTime().isAfter(LocalTime.of(16, 30))) {
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