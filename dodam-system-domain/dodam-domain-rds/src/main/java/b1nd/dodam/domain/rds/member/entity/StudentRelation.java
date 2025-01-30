package b1nd.dodam.domain.rds.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "student_relation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @NotNull
    private String relation;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_parent_id", nullable = false)
    private Parent parent;

    @Builder
    public StudentRelation(String relation, Student student, Parent parent) {
        this.relation = relation;
        this.student = student;
        this.parent = parent;
    }

}
