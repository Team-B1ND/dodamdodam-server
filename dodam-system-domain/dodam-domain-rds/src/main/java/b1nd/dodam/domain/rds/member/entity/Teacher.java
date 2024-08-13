package b1nd.dodam.domain.rds.member.entity;

import jakarta.persistence.*;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "teacher")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Teacher {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(cascade = CascadeType.ALL)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @NotNull
    private String tel;

    @NotNull
    private String position;

    @Builder
    public Teacher(Member member, String tel, String position) {
        this.member = member;
        this.tel = tel;
        this.position = position;
    }

}
