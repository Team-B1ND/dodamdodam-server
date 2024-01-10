package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    Optional<Teacher> findByMember(Member member);

}
