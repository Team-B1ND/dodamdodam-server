package b1nd.dodam.domain.rds.division.repository;

import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DivisionMemberRepository extends JpaRepository<DivisionMember, Long> {
    void deleteAllByDivision(Division division);

    DivisionMember findByDivisionAndMember(Division division, Member member);

    Boolean existsByDivisionAndMemberAndStatusNot(Division division, Member member, ApprovalStatus status);

    DivisionMember findByDivisionAndMemberAndStatus(Division division, Member member, ApprovalStatus status);

    @EntityGraph(attributePaths = {"division"})
    List<DivisionMember> findByMember(Member member);

    @EntityGraph(attributePaths = {"member"})
    List<DivisionMember> findByDivisionAndStatus(Division division, ApprovalStatus status);

    Long countByDivisionAndStatus(Division division, ApprovalStatus status);
}
