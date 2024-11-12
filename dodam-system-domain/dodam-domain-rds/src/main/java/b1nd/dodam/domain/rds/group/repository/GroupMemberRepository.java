package b1nd.dodam.domain.rds.group.repository;

import b1nd.dodam.domain.rds.group.entity.Group;
import b1nd.dodam.domain.rds.group.entity.GroupMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface GroupMemberRepository extends JpaRepository<GroupMember, Long> {
    void deleteAllByGroup(Group group);

    GroupMember findByGroupAndMember(Group group, Member member);

    @EntityGraph(attributePaths = {"group"})
    List<GroupMember> findByMemberAndStatus(Member member, ApprovalStatus status);

    @EntityGraph(attributePaths = {"member"})
    List<GroupMember> findByGroup(Group group);

}
