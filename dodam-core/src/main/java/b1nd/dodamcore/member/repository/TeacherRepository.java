package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.exception.TeacherNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    void deleteByMember(Member member);

    Optional<Teacher> findByMember(Member member);

    default Teacher getByMember(Member member) {
        return findByMember(member)
                .orElseThrow(TeacherNotFoundException::new);
    }

}
