package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.support.event.listener.EntitySyncListener;
import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "student")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(EntitySyncListener.class)
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

    @Column(columnDefinition = "BOOLEAN DEFAULT FALSE")
    private Boolean busSubscribe;

    @Builder
    public Student(int id, Member member, int grade, int room, int number, String code, Boolean busSubscribe) {
        this.id = id;
        this.member = member;
        this.grade = grade;
        this.room = room;
        this.number = number;
        this.code = code;
        this.busSubscribe = busSubscribe;
    }

    public void updateInfo(int grade, int room, int number) {
        this.grade = grade;
        this.room = room;
        this.number = number;
    }

}
