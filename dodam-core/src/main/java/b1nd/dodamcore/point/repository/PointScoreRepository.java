package b1nd.dodamcore.point.repository;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.point.domain.entity.PointScore;
import b1nd.dodamcore.point.domain.exception.PointScoreNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface PointScoreRepository extends JpaRepository<PointScore, Integer> {

    Optional<PointScore> findByStudent(Student student);

    List<PointScore> findByStudentIn(List<Student> students);

    default List<PointScore> getByStudentIn(List<Student> students) {
        List<PointScore> scores = findByStudentIn(students);

        if(students.size() == scores.size()) {
            return scores;
        }

        throw new PointScoreNotFoundException();
    }

    @EntityGraph(attributePaths = {"student.member"})
    List<PointScore> findAll();
}
