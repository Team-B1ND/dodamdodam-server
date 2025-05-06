package b1nd.dodam.domain.rds.nightstudy.entity;

import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectMemberRole;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Getter;
import lombok.AccessLevel;
import jakarta.persistence.*;
import lombok.NoArgsConstructor;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NightStudyProjectMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_project_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private NightStudyProject project;

    @NotNull
    @Enumerated(EnumType.STRING)
    private NightStudyProjectMemberRole role;

    @Builder
    public NightStudyProjectMember(Student student, NightStudyProject project, NightStudyProjectMemberRole role) {
        this.student = student;
        this.project = project;
        this.role = role;
    }

    public void modifyStatus(NightStudyProject project, Teacher teacher, ApprovalStatus status, String rejectReason) {
        this.status = status;
        this.teacher = teacher;
        this.rejectReason = rejectReason;
    }
}
