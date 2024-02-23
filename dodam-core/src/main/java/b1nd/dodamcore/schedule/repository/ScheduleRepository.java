package b1nd.dodamcore.schedule.repository;

import b1nd.dodamcore.schedule.domain.entity.Schedule;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface ScheduleRepository extends JpaRepository<Schedule, Integer> {

    @EntityGraph(attributePaths = {"targetGrades"})
    List<Schedule> findAllByOrderByIdDesc(PageRequest pageRequest);

    @Query("select s from schedule s LEFT JOIN FETCH s.targetGrades where (s.startDate >= :startDate and s.endDate <= :endDate)")
    List<Schedule> findByDateBetween(LocalDate startDate, LocalDate endDate);

    @Query("select s from schedule s LEFT JOIN FETCH s.targetGrades where s.name like concat('%', ?1, '%')")
    List<Schedule> findByNameContaining(String name);

    @Query("select s from schedule s LEFT JOIN FETCH s.targetGrades where (s.startDate <= :date and s.endDate >= :date)")
    List<Schedule> findByDate(LocalDate date);
}