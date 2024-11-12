package b1nd.dodam.domain.rds.outsleeping.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outsleeping.entity.OutSleeping;
import b1nd.dodam.domain.rds.outsleeping.exception.OutSleepingNotFoundException;
import b1nd.dodam.domain.rds.outsleeping.repository.OutSleepingRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
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
        return getByDate(now).parallelStream().toList();
    }

    public List<OutSleeping> getResidual(LocalDate now){
//        return getByDate(now).parallelStream()
        return null;
    }

    public List<OutSleeping> getByDate(LocalDate date) {
        return repository.findByDate(date);
    }

    public List<OutSleeping> getByEndAt(LocalDate endAt){
        return repository.findByEndAtGreaterThanEqual(endAt);
    }

    public List<OutSleeping> getByStudent(Student student, LocalDate now) {
        return repository.findByStudentAndEndAtGreaterThanEqual(student, now);
    }

}
