package b1nd.dodamcore.outsleeping.repository;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outsleeping.domain.entity.OutSleeping;
import b1nd.dodamcore.outsleeping.domain.exception.OutSleepingNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface OutSleepingRepository extends JpaRepository<OutSleeping, Long> {

    default OutSleeping getById(Long id) {
        return findById(id)
                .orElseThrow(OutSleepingNotFoundException::new);
    }

    @EntityGraph(attributePaths = {"student.member"})
    @Query("select o from OutSleeping o where :date between o.startAt and o.endAt")
    List<OutSleeping> findByDate(@Param("date") LocalDate date);

    @EntityGraph(attributePaths = {"student.member"})
    List<OutSleeping> findByEndAtGreaterThanEqual(@Param("endAt") LocalDate endAt);

    @EntityGraph(attributePaths = {"student.member"})
    List<OutSleeping> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDate now);

}