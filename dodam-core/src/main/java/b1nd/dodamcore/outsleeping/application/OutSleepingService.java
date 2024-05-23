package b1nd.dodamcore.outsleeping.application;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outsleeping.domain.entity.OutSleeping;
import b1nd.dodamcore.outsleeping.domain.enums.OutSleepingStatus;
import b1nd.dodamcore.outsleeping.domain.exception.OutSleepingNotFoundException;
import b1nd.dodamcore.outsleeping.repository.OutSleepingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutSleepingService {

    private final OutSleepingRepository repository;

    public void save(OutSleeping outSleeping) {
        repository.save(outSleeping);
    }

    public void delete(OutSleeping outSleeping) {
        repository.delete(outSleeping);
    }

    public OutSleeping getById(Long id) {
        return repository.findById(id)
                .orElseThrow(OutSleepingNotFoundException::new);
    }

    public List<OutSleeping> getValid(LocalDate now) {
        return getByDate(now).parallelStream()
                .filter(o -> OutSleepingStatus.ALLOWED.equals(o.getStatus()))
                .toList();
    }

    public List<OutSleeping> getByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<OutSleeping> getByStudent(Student student, LocalDate now) {
        return repository.findByStudentAndEndAtGreaterThanEqual(student, now);
    }

}
