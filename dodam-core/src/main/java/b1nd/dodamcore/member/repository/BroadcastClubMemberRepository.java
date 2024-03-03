package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.BroadcastClubMember;
import b1nd.dodamcore.member.domain.entity.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BroadcastClubMemberRepository extends JpaRepository<BroadcastClubMember, Integer> {

    boolean existsByMember(Member member);

}