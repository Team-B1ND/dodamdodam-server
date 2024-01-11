package b1nd.dodamcore.bus.repository;

import b1nd.dodamcore.bus.domain.entity.Bus;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusRepository extends JpaRepository<Bus, Integer> {

    List<Bus> findBusByLeaveTimeBetween(LocalDateTime leaveTime1, LocalDateTime localTime2);
    List<Bus> findAllByOrderByIdDesc(Pageable pageable);

    List<Bus> findByLeaveTimeAfter(LocalDateTime leaveTime);

    @Query(value = "select * from bus " +
            "where leave_time >= :now " +
            "and id in (select fk_bus_id from bus_member where fk_student_id = :studentId)",nativeQuery = true)
    Bus findBusByStudent(@Param("now") LocalDateTime now, @Param("studentId") int studentId);

    @Query(value = "select * from bus " +
            "where leave_time BETWEEN :start and :end " +
            "and id in ( SELECT fk_bus_id from bus_member where fk_student_id = :studentId)", nativeQuery = true)
    List<Bus> findBusByMonth(
            @Param("start") LocalDateTime start,
            @Param("end") LocalDateTime end,
            @Param("studentId") int studentId
    );

    @Query("select b from bus b where b.leaveTime LIKE concat(:localDate, '%')")
    List<Bus> findAllByLeaveTime(LocalDate localDate);
}
