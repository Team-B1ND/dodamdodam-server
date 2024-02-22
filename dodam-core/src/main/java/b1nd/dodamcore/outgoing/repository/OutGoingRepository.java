package b1nd.dodamcore.outgoing.repository;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outgoing.domain.entity.OutGoing;
import b1nd.dodamcore.outgoing.domain.exception.OutGoingNotFoundException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface OutGoingRepository extends JpaRepository<OutGoing, Long> {

    default OutGoing getById(Long id) {
        return findById(id)
                .orElseThrow(OutGoingNotFoundException::new);
    }

    @EntityGraph(attributePaths = {"student", "student.member"})
    @Query("select o from OutGoing o where o.endAt between :startOfTheDay and :endOfTheDay")
    List<OutGoing> findByDate(@Param("startOfTheDay")LocalDateTime startOfTheDay, @Param("endOfTheDay") LocalDateTime endOfTheDay);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<OutGoing> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDateTime now);

}