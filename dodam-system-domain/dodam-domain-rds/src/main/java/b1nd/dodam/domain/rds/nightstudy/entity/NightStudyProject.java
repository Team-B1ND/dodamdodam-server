package b1nd.dodam.domain.rds.nightstudy.entity;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.annotation.Nullable;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NightStudyProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NightStudyProjectType type;

    @NotNull
    private String name;

    @NotNull
    private String description;

    private String rejectReason;

    @NotNull
    private LocalDate startAt;

    @NotNull
    private LocalDate endAt;

    @Enumerated(EnumType.STRING)
    private NightStudyProjectRoom room;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    @Builder
    public NightStudyProject(NightStudyProjectType type, String name, String description, LocalDate startAt, LocalDate endAt, Teacher teacher) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = ApprovalStatus.PENDING;
        this.teacher = teacher;
    }

    public void reject(Teacher teacher, String rejectReason) {
        this.teacher = teacher;
        this.rejectReason = rejectReason;
        this.status = ApprovalStatus.REJECTED;
    }

    public void modifyStatus(Teacher teacher, NightStudyProjectRoom room) {
        this.teacher = teacher;
        this.room = room;
        this.status = ApprovalStatus.ALLOWED;
    }

    public void modifyStatus(Teacher teacher, ApprovalStatus status) {
        this.status = status;
        this.teacher = teacher;
    }
}
