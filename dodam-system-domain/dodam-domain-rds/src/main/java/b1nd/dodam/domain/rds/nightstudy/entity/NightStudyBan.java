package b1nd.dodam.domain.rds.nightstudy.entity;

import java.time.LocalDate;
import lombok.Getter;
import lombok.Builder;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;

import b1nd.dodam.domain.rds.member.entity.Student;

@Entity(name = "night_study_ban")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class NightStudyBan {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    private String reason;

    @NotNull
    private LocalDate started;

    @NotNull
    private LocalDate ended;

    @Builder
    public NightStudyBan(Student student, String reason, LocalDate started, LocalDate ended) {
        this.student = student;
        this.reason = reason;
        this.started = started;
        this.ended = ended;
    }

    public void updateInfo(String reason, LocalDate started, LocalDate ended) {
        this.reason = reason;
        this.started = started;
        this.ended = ended;
    }

    public Integer getStudentId() {
        return student.getId();
    }
}
