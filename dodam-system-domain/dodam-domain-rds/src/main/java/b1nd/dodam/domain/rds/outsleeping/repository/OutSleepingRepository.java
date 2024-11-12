package b1nd.dodam.domain.rds.outsleeping.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outsleeping.entity.OutSleeping;
import b1nd.dodam.domain.rds.outsleeping.exception.OutSleepingNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface OutSleepingRepository extends JpaRepository<OutSleeping, Long> {

    default OutSleeping getById(Long id) {
        return findById(id)
                .orElseThrow(OutSleepingNotFoundException::new);
    }

    @EntityGraph(attributePaths = {"student.member"})
    @Query("select o from OutSleeping o where :date between o.startAt and o.endAt " +
            "and o.endAt > :date " +
            "and o.status = 'ALLOWED' " +
            "order by o.student.grade, o.student.room, o.student.number")
    List<OutSleeping> findByDate(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"student.member"})
    List<OutSleeping> findByEndAtGreaterThanEqual(LocalDate endAt);

    @EntityGraph(attributePaths = {"student.member"})
    List<OutSleeping> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDate now);

}
