package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    void deleteAllByMember(Member member);
}
