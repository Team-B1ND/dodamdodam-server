package b1nd.dodam.domain.rds.bus.service;

import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplicant;
import b1nd.dodam.domain.rds.bus.exception.BusAlreadyAppliedPositionException;
import b1nd.dodam.domain.rds.bus.exception.BusApplicantNotFoundException;
import b1nd.dodam.domain.rds.bus.repository.BusApplicantRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusApplicantService {
    private final BusApplicantRepository busApplicantRepository;

    public List<BusApplicant> getByBus(Bus bus) {
        return busApplicantRepository.findByBus(bus);
    }

    public void existsBySeatsAndBus(Bus bus, int seat) {
        if (busApplicantRepository.findByBusAndSeat(bus, seat) != null)
            throw new BusAlreadyAppliedPositionException();
    }

    public void deleteByBus(long busId) {
        busApplicantRepository.deleteByBus_Id(busId);
    }

    public BusApplicant getByStudent(Student student) {
        return busApplicantRepository.findByStudent(student)
            .orElseThrow(BusApplicantNotFoundException::new);
    }

    public BusApplicant getByStudentOrNull(Student student) {
        return busApplicantRepository.findByStudent(student).orElse(null);
    }

    public void save(BusApplicant busApplicant) {
        busApplicantRepository.save(busApplicant);
    }
}
