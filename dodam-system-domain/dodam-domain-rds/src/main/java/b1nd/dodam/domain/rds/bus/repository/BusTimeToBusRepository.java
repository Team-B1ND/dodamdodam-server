package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.BusTime;
import b1nd.dodam.domain.rds.bus.entity.BusTimeToBus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface BusTimeToBusRepository extends JpaRepository<BusTimeToBus, Integer> {
    @EntityGraph(attributePaths = {"busTime", "bus"})
    List<BusTimeToBus> findAllByBusTime(@Param("busTime") BusTime busTime);

    BusTimeToBus findByBus_Id(int id);
}
