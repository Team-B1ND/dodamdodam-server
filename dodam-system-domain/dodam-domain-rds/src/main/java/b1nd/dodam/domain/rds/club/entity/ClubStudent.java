package b1nd.dodam.domain.rds.club.entity;

import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStudentStatus;
import b1nd.dodam.domain.rds.member.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

@Getter
@Entity(name = "club_student")
@Table(uniqueConstraints = {
        @UniqueConstraint(
                name = "unique_student_club_index",
                columnNames = {
                        "fk_student_id",
                        "fk_club_id"
                }
        )
})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class ClubStudent {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubPermission permission;

    @NotNull
    @Enumerated(EnumType.STRING)
    private ClubStudentStatus clubStatus;

    private int choiceNumber;

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

    @Builder
    public ClubStudent(Student student, Club club, int choiceNumber, ClubStudentStatus clubStatus, ClubPermission permission) {
        this.student = student;
        this.club = club;
        this.choiceNumber = choiceNumber;
        this.clubStatus = clubStatus;
        this.permission = permission;
    }

    public void modifyStatus(ClubStudentStatus status) {
        this.clubStatus = status;
    }
}
