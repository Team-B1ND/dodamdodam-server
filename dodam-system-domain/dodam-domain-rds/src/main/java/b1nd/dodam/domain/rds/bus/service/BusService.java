package b1nd.dodam.domain.rds.bus.service;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.exception.BusApplicantNotFoundException;
import b1nd.dodam.domain.rds.bus.repository.BusApplicantRepository;
import b1nd.dodam.domain.rds.bus.repository.BusRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusService {
    private final BusApplicantRepository busApplicantRepository;
    private final BusRepository busRepository;

    public List<Bus> getAll() {
        return busRepository.findAll();
    }

    public Bus getById(Long id) {
        return busRepository.findById(id)
            .orElseThrow(BusApplicantNotFoundException::new);
    }

    public void save(Bus bus) {
        busRepository.save(bus);
    }

    public void deleteById(Long busId) {
        busApplicantRepository.deleteByBus_Id(busId);
        busRepository.deleteById(busId);
    }
}
