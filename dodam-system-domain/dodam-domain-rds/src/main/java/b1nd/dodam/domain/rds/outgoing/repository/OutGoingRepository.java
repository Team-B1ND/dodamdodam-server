package b1nd.dodam.domain.rds.outgoing.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outgoing.entity.OutGoing;
import b1nd.dodam.domain.rds.outgoing.exception.OutGoingNotFoundException;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface OutGoingRepository extends JpaRepository<OutGoing, Long> {

    default OutGoing getById(Long id) {
        return findById(id)
                .orElseThrow(OutGoingNotFoundException::new);
    }

    @EntityGraph(attributePaths = {"student.member"})
    @Query("select o from OutGoing o where o.endAt between :startOfTheDay and :endOfTheDay")
    List<OutGoing> findByDate(@Param("startOfTheDay")LocalDateTime startOfTheDay, @Param("endOfTheDay") LocalDateTime endOfTheDay);

    @EntityGraph(attributePaths = {"student.member"})
    List<OutGoing> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDateTime now);

    Long countByIsDinnerAndStatusAndStartAtBetween(Boolean isDinner, ApprovalStatus status, LocalDateTime startOfDay, LocalDateTime endOfDay);
}
