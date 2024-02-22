package b1nd.dodamcore.member.domain.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "student")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Student {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @NotNull
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    private Member member;

    @NotNull
    private int grade;

    @NotNull
    private int room;

    @NotNull
    private int number;

    public void updateStudentInfo(int grade, int room, int number) {
        this.grade = grade;
        this.room = room;
        this.number = number;
    }

    @Builder
    public Student(Integer id, Member member, int grade, int room, int number) {
        this.id = id;
        this.member = member;
        this.grade = grade;
        this.room = room;
        this.number = number;
    }

}
