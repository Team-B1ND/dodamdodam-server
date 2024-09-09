package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.exception.StudentNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StudentRepository extends JpaRepository<Student, Integer> {

    void deleteByMember(Member member);

    @EntityGraph(attributePaths = {"member"})
    Optional<Student> findByMember(Member member);

    default Student getById(int id) {
        return findById(id)
                .orElseThrow(StudentNotFoundException::new);
    }

    default Student getByMember(Member member) {
        return findByMember(member)
                .orElseThrow(StudentNotFoundException::new);
    }

    default List<Student> getByIds(List<Integer> ids) {
        List<Student> students = findAllById(ids);

        if(students.size() == ids.size()) {
            return students;
        }

        throw new StudentNotFoundException();
    }

    Long countByMemberStatus(ActiveStatus memberStatus);

}