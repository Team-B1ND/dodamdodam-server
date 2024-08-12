package b1nd.dodam.domain.rds.bus.service;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.bus.entity.Bus;
import b1nd.dodam.domain.rds.bus.entity.BusApplication;
import b1nd.dodam.domain.rds.bus.exception.BusMemberNotFoundException;
import b1nd.dodam.domain.rds.bus.repository.BusApplicationRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class BusApplicationService {

    private final BusApplicationRepository repository;

    public void save(BusApplication busApplication) {
        repository.save(busApplication);
    }

    public void delete(BusApplication busApplication) {
        repository.delete(busApplication);
    }

    public BusApplication getMy(Student student) {
        return repository.findByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime())
                .orElseThrow(BusMemberNotFoundException::new);
    }

    public boolean hasMy(Student student) {
        return repository.existsByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime());
    }

    public List<BusApplication> getByBus(Bus bus) {
        return repository.findByBusOrderByStudentAsc(bus);
    }

}
