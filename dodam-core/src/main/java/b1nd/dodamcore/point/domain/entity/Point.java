package b1nd.dodamcore.point.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
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
public class Point extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_reason_id", nullable = false)
    private PointReason reason;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_teacher_id", nullable = false)
    private Teacher teacher;

    @NotNull
    private LocalDate issueAt;

    @Builder
    public Point(PointReason reason, Student student, Teacher teacher, LocalDate issueAt) {
        this.reason = reason;
        this.student = student;
        this.teacher = teacher;
        this.issueAt = issueAt;
    }

}
