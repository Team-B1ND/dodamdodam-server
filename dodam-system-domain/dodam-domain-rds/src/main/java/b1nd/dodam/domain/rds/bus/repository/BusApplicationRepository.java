package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.exception.BusApplicationNotFoundException;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
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

}
