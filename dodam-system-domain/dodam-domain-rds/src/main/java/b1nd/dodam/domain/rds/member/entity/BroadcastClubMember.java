package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "broadcast_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BroadcastClubMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @Builder
    public BroadcastClubMember(Member member) {
        this.member = member;
    }

}
