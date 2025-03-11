package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.BusTime;
import b1nd.dodam.domain.rds.bus.exception.BusTimNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusTimeRepository extends JpaRepository<BusTime, Integer> {

    default BusTime getById(int id){
        return findById(id).orElseThrow(BusTimNotFoundException::new);
    }

    Optional<BusTime> findById(int id);

}
