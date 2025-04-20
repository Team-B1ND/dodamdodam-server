package b1nd.dodam.domain.rds.nightstudy.entity;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity(name = "night_study_project")
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
    @JoinColumn(name = "fk_student_id")
    private Student leader;

    @Builder
    public NightStudyProject(NightStudyType type, String name, String description, LocalDate startAt, LocalDate endAt, NightStudyProjectRoom room, Student leader) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.room = room;
        this.status = ApprovalStatus.PENDING;
        this.leader = leader;
    }

    public boolean isLeader(Student student) {
        return this.leader.equals(student);
    }
}
