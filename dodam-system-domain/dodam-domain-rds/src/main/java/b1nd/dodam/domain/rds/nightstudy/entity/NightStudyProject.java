package b1nd.dodam.domain.rds.nightstudy.entity;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.*;

import java.time.LocalDate;

@Entity(name = "night_study_project")
@Table(name = "night_study_project")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NightStudyProject {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    private NightStudyType type;

    @NotNull
    private String name;

    @NotNull
    private String description;

    @NotNull
    private LocalDate startAt;

    @NotNull
    private LocalDate endAt;

    @NotNull
    private NightStudyProjectRoom room;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_leader_id", nullable = false)
    private Student leader;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id")
    private Teacher teacher;

    private String rejectReason;

    @Builder
    public NightStudyProject(NightStudyType type, String name, String description, LocalDate startAt, LocalDate endAt, NightStudyProjectRoom room, Student leader, Teacher teacher) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.room = room;
        this.status = ApprovalStatus.PENDING;
        this.leader = leader;
        this.teacher = teacher;
    }

    public boolean isLeader(Student student) {
        return this.leader.equals(student);
    }

    public void modifyStatus(Teacher teacher, ApprovalStatus status, String rejectReason) {
        this.status = status;
        this.teacher = teacher;
        this.rejectReason = rejectReason;
    }
}
