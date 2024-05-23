package b1nd.dodamcore.outgoing.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.outgoing.domain.enums.OutGoingStatus;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.outgoing.domain.exception.InvalidOutGoingDurationException;
import b1nd.dodamcore.outgoing.domain.exception.NotOutGoingApplicantException;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class OutGoing extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotBlank
    private String reason;

    @NotNull
    private LocalDateTime startAt;

    @NotNull
    private LocalDateTime endAt;

    @NotNull
    @Enumerated(value = EnumType.STRING)
    private OutGoingStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    private String rejectReason;

    @Builder
    public OutGoing(String reason, LocalDateTime startAt, LocalDateTime endAt, Student student) {
        isInvalidPeriod(startAt, endAt);

        this.reason = reason;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = OutGoingStatus.PENDING;
        this.student = student;
    }

    public void modifyStatus(Teacher teacher, OutGoingStatus status, String rejectReason) {
        this.status = status;
        this.teacher = teacher;
        this.rejectReason = rejectReason;
    }

    public boolean isNotApplicant(Student student) {
        return this.student.getId() != student.getId();
    }

    private void isInvalidPeriod(LocalDateTime startAt, LocalDateTime endAt) {
        if(startAt.isAfter(endAt) || !startAt.toLocalDate().equals(endAt.toLocalDate())) {
            throw new InvalidOutGoingDurationException();
        }
    }

}