package b1nd.dodam.domain.rds.nightstudy.entity;

import b1nd.dodam.domain.rds.member.entity.DormitoryManageMember;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectRoom;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
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
public class NightStudyProject extends BaseEntity {
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

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_dormitory_manage_member_id")
    private DormitoryManageMember dormitoryManageMember;

    @Builder
    public NightStudyProject(NightStudyProjectType type, String name, String description, LocalDate startAt, LocalDate endAt, Teacher teacher, DormitoryManageMember dormitoryManageMember) {
        this.type = type;
        this.name = name;
        this.description = description;
        this.startAt = startAt;
        this.endAt = endAt;
        this.status = ApprovalStatus.PENDING;
        this.teacher = teacher;
        this.dormitoryManageMember = dormitoryManageMember;
    }

    public void modifyStatusByTeacher(Teacher teacher, ApprovalStatus status, NightStudyProjectRoom room, String rejectReason) {
        this.teacher = teacher;
        this.status = status;
        this.room = room;
        this.rejectReason = rejectReason;
    }

    public void modifyStatusByDormitoryManageMember(DormitoryManageMember member, ApprovalStatus status, NightStudyProjectRoom room, String rejectReason) {
        this.dormitoryManageMember = member;
        this.status = status;
        this.room = room;
        this.rejectReason = rejectReason;
    }
}
