package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity(name = "club_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubPermission permission;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubStatus clubStatus;

    @Enumerated(EnumType.STRING)
    private ClubPriority priority;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Student student;

    @NotNull
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_club_id", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    private Club club;

    @Column(columnDefinition = "TEXT")
    private String introduction;

    @Builder
    public ClubMember(Student student, Club club, ClubPriority priority, ClubStatus clubStatus, ClubPermission permission, String introduction) {
        this.student = student;
        this.club = club;
        this.priority = priority;
        this.clubStatus = clubStatus;
        this.permission = permission;
        this.introduction = introduction;
    }

    public void modifyStatus(ClubStatus clubStatus) {
        this.clubStatus = clubStatus;
    }
}
