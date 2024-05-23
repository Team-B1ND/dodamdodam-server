package b1nd.dodamcore.outgoing.application;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.outgoing.domain.entity.OutGoing;
import b1nd.dodamcore.outgoing.domain.exception.OutGoingNotFoundException;
import b1nd.dodamcore.outgoing.repository.OutGoingRepository;
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
