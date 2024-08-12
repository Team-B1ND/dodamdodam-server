package b1nd.dodam.domain.rds.point.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.exception.PointScoreNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

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
