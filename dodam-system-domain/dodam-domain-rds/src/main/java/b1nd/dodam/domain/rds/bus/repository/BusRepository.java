package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import b1nd.dodam.domain.rds.bus.exception.BusNotFoundException;
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

    List<Bus> findBusByStatusAndLeaveTimeBetween(BusStatus status, LocalDateTime startAt, LocalDateTime endAt);

    List<Bus> findAllByOrderByIdDesc(Pageable pageable);

    @Query(value = """
        SELECT id, leave_time, status FROM bus b
        WHERE b.leave_time >= :now
        AND b.status = :status
        AND EXISTS (
            SELECT 1
            FROM bus_member bm
            WHERE bm.fk_student_id = :studentId
            AND bm.fk_bus_id = b.id
        )""",nativeQuery = true)
    Bus findBusByStatusAndStudent(@Param("status") BusStatus status, @Param("now") LocalDateTime now, @Param("studentId") int studentId);

    @Query("select b from bus b where b.leaveTime LIKE concat(:localDate, '%')")
    List<Bus> findAllByLeaveTime(LocalDate localDate);

    List<Bus> findByStatus(BusStatus status);

    default Bus getByIdForUpdate(int id) {
        return findByIdWithPessimisticLock(id)
                .orElseThrow(BusNotFoundException::new);
    }

    default Bus getById(int id) {
        return findById(id).orElseThrow(BusNotFoundException::new);
    }

}
