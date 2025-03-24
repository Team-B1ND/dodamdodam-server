package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.BusTime;
import b1nd.dodam.domain.rds.bus.entity.BusTimeToBus;
import b1nd.dodam.domain.rds.bus.exception.BusTimeUnableException;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface BusTimeToBusRepository extends JpaRepository<BusTimeToBus, Integer> {
    @EntityGraph(attributePaths = {"busTime", "bus"})
    List<BusTimeToBus> findAllByBusTime(@Param("busTime") BusTime busTime);

    default BusTimeToBus getByBusId(int id){
        return findBusTimeToBusByBus_Id(id).orElseThrow(BusTimeUnableException::new);
    }

    Optional<BusTimeToBus> findBusTimeToBusByBus_Id(int id);
}
