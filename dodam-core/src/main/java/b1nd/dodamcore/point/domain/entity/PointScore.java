package b1nd.dodamcore.point.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import b1nd.dodamcore.member.domain.entity.Student;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PointScore extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @Embedded
    @AttributeOverride(name = "bonus", column = @Column(name = "dor_bonus"))
    @AttributeOverride(name = "minus", column = @Column(name = "dor_minus"))
    @AttributeOverride(name = "offset", column = @Column(name = "dor_offset"))
    private Score dormitoryScore;

    @NotNull
    @Embedded
    @AttributeOverride(name = "bonus", column = @Column(name = "sch_bonus"))
    @AttributeOverride(name = "minus", column = @Column(name = "sch_minus"))
    @AttributeOverride(name = "offset", column = @Column(name = "sch_offset"))
    private Score schoolScore;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_student_id", unique = true)
    private Student student;

    @Builder
    public PointScore(Student student) {
        this.dormitoryScore = new Score();
        this.schoolScore = new Score();
        this.student = student;
    }

    public void issue(PointReason reason) {
        switch (reason.getPointType()) {
            case DORMITORY -> dormitoryScore.issue(reason.getScoreType(), reason.getScore());
            case SCHOOL -> schoolScore.issue(reason.getScoreType(), reason.getScore());
        }
    }

    public void cancel(PointReason reason) {
        switch (reason.getPointType()) {
            case DORMITORY -> dormitoryScore.cancel(reason.getScoreType(), reason.getScore());
            case SCHOOL -> schoolScore.cancel(reason.getScoreType(), reason.getScore());
        }
    }

}
