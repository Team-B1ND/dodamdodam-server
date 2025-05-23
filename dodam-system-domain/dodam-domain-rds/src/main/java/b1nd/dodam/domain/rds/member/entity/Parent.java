package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.support.event.listener.EntitySyncListener;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "parent")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@EntityListeners(EntitySyncListener.class)
public class Parent {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @Builder
    public Parent(Member member) {
        this.member = member;
    }

}
