package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplicant;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface BusApplicantRepository extends JpaRepository<BusApplicant, Long> {
    void deleteByBus_Id(Long busId);
    List<BusApplicant> findByBus(Bus bus);

    BusApplicant findByBusAndSeat(Bus bus, int seats);

    @EntityGraph(attributePaths = {"bus"})
    Optional<BusApplicant> findByStudent(Student student);
}
