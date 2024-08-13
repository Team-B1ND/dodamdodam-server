package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import jakarta.persistence.LockModeType;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Lock;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface BusRepository extends JpaRepository<Bus, Integer> {

    @Lock(LockModeType.PESSIMISTIC_WRITE)
    @Query("select b from bus b where b.id = :id")
    Optional<Bus> findByIdWithPessimisticLock(@Param("id") int id);

    List<Bus> findBusByLeaveTimeBetween(LocalDateTime startAt, LocalDateTime endAt);

    List<Bus> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = "select * from bus " +
            "where leave_time >= :now " +
            "and id in (select fk_bus_id from bus_member where fk_student_id = :studentId)",nativeQuery = true)
    Bus findBusByStudent(@Param("now") LocalDateTime now, @Param("studentId") int studentId);

    @Query("select b from bus b where b.leaveTime LIKE concat(:localDate, '%')")
    List<Bus> findAllByLeaveTime(LocalDate localDate);

}
