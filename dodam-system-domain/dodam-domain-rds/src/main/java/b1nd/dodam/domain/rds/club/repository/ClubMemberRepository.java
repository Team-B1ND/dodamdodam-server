package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    void deleteAllByMemberAndClub_Type(Member member, ClubType clubType);
    List<ClubMember> findAllByMemberAndClub_Type(Member member, ClubType clubType);
}
