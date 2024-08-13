package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.exception.TeacherNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    void deleteByMember(Member member);

    Optional<Teacher> findByMember(Member member);

    default Teacher getByMember(Member member) {
        return findByMember(member)
                .orElseThrow(TeacherNotFoundException::new);
    }

}
