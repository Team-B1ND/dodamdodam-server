package b1nd.dodamcore.bus.repository;

import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.member.domain.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface BusMemberRepository extends JpaRepository<BusMember, Integer> {

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<BusMember> findByBusOrderByStudentAsc(Bus bus);

    List<BusMember> findByBus(Bus bus);

    Optional<BusMember> findByStudentAndBus_Id(Student student, int busId);

    @EntityGraph(attributePaths = {"bus"})
    Optional<BusMember> findByStudentAndBus_LeaveTimeAfter(Student student, LocalDateTime now);

    boolean existsByStudentAndBus_LeaveTimeAfter(Student student, LocalDateTime now);
}
