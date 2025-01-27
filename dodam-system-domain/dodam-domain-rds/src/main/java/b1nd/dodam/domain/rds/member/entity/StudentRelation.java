package b1nd.dodam.domain.rds.member.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "student_relation")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class StudentRelation {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id ;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", nullable = false)
    private Student student;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_parent_id", nullable = false)
    private Parent parent;

}
