package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.enumeration.BusApplicationStatus;
import b1nd.dodam.domain.rds.bus.enumeration.BusStatus;
import b1nd.dodam.domain.rds.bus.exception.BusApplicationNotFoundException;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BusApplicationRepository extends JpaRepository<BusApplication, Integer> {

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<BusApplication> findByBusOrderByStudentAsc(Bus bus);

    @EntityGraph(attributePaths = {"bus"})
    Optional<BusApplication> findByStudentAndBus_LeaveTimeAfter(Student student, LocalDateTime now);

    boolean existsByStudentAndBus_LeaveTimeAfter(Student student, LocalDateTime now);

    default BusApplication getByStudentAndBus_LeaveTimeAfter(Student student, LocalDateTime now) {
        return findByStudentAndBus_LeaveTimeAfter(student, now)
                .orElseThrow(BusApplicationNotFoundException::new);
    }

    default BusApplication getBusApplicationByBus(Bus bus){
        return findByBus(bus).orElseThrow(BusApplicationNotFoundException::new);
    }

    List<BusApplication> findByBusAndStatusNot(Bus bus, BusApplicationStatus status);

    Optional<BusApplication> findByStatusAndStudent_Member(BusApplicationStatus status, Member member);

    default BusApplication getByStatusAndMember(BusApplicationStatus status, Member member){
        return findByStatusAndStudent_Member(status, member)
                .orElseThrow(BusApplicationNotFoundException::new);
    }

    @Query("""
        select bm.bus from bus_member bm
        where bm.bus.leaveAt >= :now
        and bm.bus.leaveTime >= :time
        and bm.bus.status = :status
        and bm.student.id = :studentId
    """)
    Bus findBusByStatusAndStudent(@Param("status") BusStatus status,
                                  @Param("now") LocalDate now,
                                  @Param("time")LocalTime time,
                                  @Param("studentId") int studentId);

    List<BusApplication> findByBus_IdAndStatus(int busId, BusApplicationStatus status);

    Optional<BusApplication> findByBus(Bus bus);

}
