package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.enums.AuthStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, String> {

    Optional<Member> findById(String id);

    Optional<Member> findByIdAndPw(String id, String pw);

    List<Member> findByJoinDateAfter(LocalDateTime joinDate);

    List<Member> findByStatus(AuthStatus status);

    //Optional<Member> findByStudent(Student student);
}
