package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.BroadcastClubMember;
import b1nd.dodam.domain.rds.member.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;

public interface BroadcastClubMemberRepository extends JpaRepository<BroadcastClubMember, Integer> {

    boolean existsByMember(Member member);

    void deleteByMember(Member member);
}
