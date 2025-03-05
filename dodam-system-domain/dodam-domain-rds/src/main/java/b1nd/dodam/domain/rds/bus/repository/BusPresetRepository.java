package b1nd.dodam.domain.rds.bus.repository;

import b1nd.dodam.domain.rds.bus.entity.BusPreset;
import b1nd.dodam.domain.rds.bus.exception.BusPresetNotFoundException;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface BusPresetRepository extends JpaRepository<BusPreset, Integer> {
    default BusPreset getById(int id){
        return findById(id)
                .orElseThrow(BusPresetNotFoundException::new);
    }
    Optional<BusPreset> findById(int id);
}
