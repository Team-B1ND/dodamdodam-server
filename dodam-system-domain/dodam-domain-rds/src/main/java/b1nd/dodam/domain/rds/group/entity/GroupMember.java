package b1nd.dodam.domain.rds.group.entity;

import b1nd.dodam.domain.rds.group.enumeration.GroupPermission;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity(name = "GroupMember")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class GroupMember {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    private GroupPermission permission;

    @Enumerated(EnumType.STRING)
    private ApprovalStatus status;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Member member;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "fk_member_id", nullable = false)
    private Group group;

    @Builder
    public GroupMember(GroupPermission permission, ApprovalStatus status, Member member, Group group) {
        this.permission = permission;
        this.status = status;
        this.member = member;
        this.group = group;
    }

    public void modifyStatus(ApprovalStatus status) {
        this.status = status;
    }
}
