package b1nd.dodamcore.member.domain.entity;

import b1nd.dodamcore.common.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity(name = "broadcast_member")
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class BroadcastClubMember extends BaseEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id")
    private Member member;

    @Builder
    public BroadcastClubMember(Member member) {
        this.member = member;
    }

}