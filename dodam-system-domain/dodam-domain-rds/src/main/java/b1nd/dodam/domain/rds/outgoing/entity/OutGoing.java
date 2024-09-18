package b1nd.dodam.domain.rds.outgoing.entity;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.outgoing.exception.InvalidOutGoingDurationException;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
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
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    private String rejectReason;

    @NotNull
    private Boolean dinnerOrNot;

    @Builder
    public OutGoing(String reason, LocalDateTime startAt, LocalDateTime endAt, Student student, Boolean dinnerOrNot) {
        isInvalidPeriod(startAt, endAt);

        this.reason = reason;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = ApprovalStatus.PENDING;
        this.student = student;
        this.dinnerOrNot = dinnerOrNot;
    }

    public void modifyStatus(Teacher teacher, ApprovalStatus status, String rejectReason) {
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
