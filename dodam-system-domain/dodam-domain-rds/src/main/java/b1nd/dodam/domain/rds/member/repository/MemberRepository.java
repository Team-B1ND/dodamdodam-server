package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);

    Optional<Member> findByIdAndPw(String id, String pw);

    List<Member> findByCreatedAtAfter(LocalDateTime createdAt);

    @Query("SELECT m FROM member m LEFT JOIN student s ON m.id = s.member.id " +
            "WHERE m.status = :status " +
            "ORDER BY CASE WHEN s.id IS NOT NULL THEN 0 ELSE 1 END ASC, " +
            "s.grade ASC, s.room ASC, s.number ASC")
    List<Member> findByStatusOrderedByStudent(ActiveStatus status);


    List<Member> findByNameContains(String name);

}
