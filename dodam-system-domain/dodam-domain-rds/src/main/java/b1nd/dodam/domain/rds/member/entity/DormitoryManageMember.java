package b1nd.dodam.domain.rds.member.entity;

import b1nd.dodam.domain.rds.support.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "dormitory_manage_member")
@NoArgsConstructor
public class DormitoryManageMember extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @Builder
    public DormitoryManageMember(Member member) {
        this.member = member;
    }
}
