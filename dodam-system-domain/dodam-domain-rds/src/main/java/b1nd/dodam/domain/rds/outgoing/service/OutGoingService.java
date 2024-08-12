package b1nd.dodam.domain.rds.outgoing.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.outgoing.entity.OutGoing;
import b1nd.dodam.domain.rds.outgoing.exception.OutGoingNotFoundException;
import b1nd.dodam.domain.rds.outgoing.repository.OutGoingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutGoingService {

    private final OutGoingRepository repository;

    public void save(OutGoing outGoing) {
        repository.save(outGoing);
    }

    public void delete(OutGoing outGoing) {
        repository.delete(outGoing);
    }

    public OutGoing getById(Long id) {
        return repository.findById(id)
                .orElseThrow(OutGoingNotFoundException::new);
    }

    public List<OutGoing> getByDate(LocalDateTime startDay, LocalDateTime endDay) {
        return repository.findByDate(startDay, endDay);
    }

    public List<OutGoing> getByStudent(Student student, LocalDateTime now) {
        return repository.findByStudentAndEndAtGreaterThanEqual(student, now);
    }

}
