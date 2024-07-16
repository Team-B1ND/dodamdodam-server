package b1nd.dodamcore.bus.application;

import b1nd.dodamcore.bus.domain.entity.Bus;
import b1nd.dodamcore.bus.domain.entity.BusMember;
import b1nd.dodamcore.bus.domain.exception.BusMemberNotFoundException;
import b1nd.dodamcore.bus.repository.BusMemberRepository;
import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.domain.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

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

    public BusMember getMy(Student student) {
        return repository.findByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime())
                .orElseThrow(BusMemberNotFoundException::new);
    }

    public boolean hasMy(Student student) {
        return repository.existsByStudentAndBus_LeaveTimeAfter(student, ZonedDateTimeUtil.nowToLocalDateTime());
    }

    public List<BusMember> getByBus(Bus bus) {
        return repository.findByBusOrderByStudentAsc(bus);
    }

}
