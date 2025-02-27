package b1nd.dodam.domain.rds.member.entity;

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

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @NotNull
    private int grade;

    @NotNull
    private int room;

    @NotNull
    private int number;

    @Column(unique = true)
    private String code;

    private Boolean canBusBoard;

    @Builder
    public Student(int id, Member member, int grade, int room, int number, String code, Boolean canBusBoard) {
        this.id = id;
        this.member = member;
        this.grade = grade;
        this.room = room;
        this.number = number;
        this.code = code;
        this.canBusBoard = canBusBoard;
    }

    public void updateInfo(int grade, int room, int number) {
        this.grade = grade;
        this.room = room;
        this.number = number;
    }

}
