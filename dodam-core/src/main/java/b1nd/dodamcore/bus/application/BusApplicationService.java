package b1nd.dodamcore.bus.application;

import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.bus.repository.BusMemberRepository;
import b1nd.dodamcore.member.domain.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class BusApplicationService {

    private final BusMemberRepository repository;

    public void save(BusMember busMember) {
        repository.save(busMember);
    }

    public void delete(BusMember busMember) {
        repository.delete(busMember);
    }

    public Optional<BusMember> findValidApplication(Student student, LocalDateTime now) {
        return repository.findByStudentAndBus_LeaveTimeAfter(student, now);
    }

    public boolean hasValidApplication(Student student, LocalDateTime now) {
        return repository.existsByStudentAndBus_LeaveTimeAfter(student, now);
    }

    public List<BusMember> getByBus(Bus bus) {
        return repository.findByBusOrderByStudentAsc(bus);
    }

}
