package b1nd.dodam.domain.rds.point.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.exception.PointScoreNotFoundException;
import jakarta.persistence.LockModeType;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface PointScoreRepository extends JpaRepository<PointScore, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ps FROM PointScore ps WHERE ps.student = :student")
    Optional<PointScore> findByStudentWithPessimisticLock(Student student);

    Optional<PointScore> findByStudent(Student student);

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("SELECT ps FROM PointScore ps WHERE ps.student IN :students")
    List<PointScore> findByStudentInWithPessimisticLock(List<Student> students);

    @EntityGraph(attributePaths = {"student.member"})
    List<PointScore> findAll();

    default PointScore getByStudentForUpdate(Student student) {
        return findByStudentWithPessimisticLock(student)
                .orElseThrow(PointScoreNotFoundException::new);
    }

    default List<PointScore> getByStudentInForUpdate(List<Student> students) {
        List<PointScore> scores = findByStudentInWithPessimisticLock(students);

        if (students.size() == scores.size()) {
            return scores;
        }

        throw new PointScoreNotFoundException();
    }

}
