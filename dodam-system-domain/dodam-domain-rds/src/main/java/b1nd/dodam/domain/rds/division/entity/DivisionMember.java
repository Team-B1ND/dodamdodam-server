package b1nd.dodam.domain.rds.division.entity;

import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

@Getter
@Entity(name = "division_member")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class DivisionMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private DivisionPermission permission;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_division_id", nullable = false)
    private Division division;

    @Builder
    public DivisionMember(DivisionPermission permission, ApprovalStatus status, Member member, Division division) {
        this.permission = permission;
        this.status = status;
        this.member = member;
        this.division = division;
    }

    public void modifyStatus(ApprovalStatus status) {
        this.status = status;
    }
}
