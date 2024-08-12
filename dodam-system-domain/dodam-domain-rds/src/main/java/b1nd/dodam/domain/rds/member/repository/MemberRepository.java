package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);

    Optional<Member> findByIdAndPw(String id, String pw);

    List<Member> findByCreatedAtAfter(LocalDateTime createdAt);

    List<Member> findByStatus(ActiveStatus status);

    List<Member> findByNameContains(String name);

}
