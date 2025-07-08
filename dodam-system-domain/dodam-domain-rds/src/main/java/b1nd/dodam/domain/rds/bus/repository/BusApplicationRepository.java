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
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

public interface BusApplicationRepository extends JpaRepository<BusApplication, Integer> {

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<BusApplication> findByBusOrderByStudentAsc(Bus bus);

    @Query("""
    select ba from bus_member ba
    where ba.student = :student
    and (ba.bus.leaveAt > :leaveAt
        or (ba.bus.leaveAt = :leaveAt and ba.bus.leaveTime > :leaveTime))
""")
    Optional<BusApplication> findValidBusApplication(
            @Param("student") Student student,
            @Param("leaveAt") LocalDate leaveAt,
            @Param("leaveTime") LocalTime leaveTime
    );

    boolean existsByStudentAndBus_LeaveAtAfterAndBus_LeaveTimeAfter(Student student,  LocalDate nowAt, LocalTime nowTime);

    default BusApplication getValidBusApplication(Student student, LocalDate leaveAt, LocalTime leaveTime) {
        return findValidBusApplication(student, leaveAt, leaveTime)
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

    void deleteAllByBus_Id(int busId);
}
