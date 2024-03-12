package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.exception.StudentNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @EntityGraph(attributePaths = {"member"})
    Optional<Student> findByMember(Member member);

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

}