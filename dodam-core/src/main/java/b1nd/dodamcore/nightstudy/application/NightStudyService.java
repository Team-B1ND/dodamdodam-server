package b1nd.dodamcore.nightstudy.application;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import b1nd.dodamcore.nightstudy.domain.exception.NightStudyNotFoundException;
import b1nd.dodamcore.nightstudy.repository.NightStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyService {

    private final NightStudyRepository repository;

    public void save(NightStudy nightStudy) {
        repository.save(nightStudy);
    }

    public void delete(NightStudy nightStudy) {
        repository.delete(nightStudy);
    }

    public NightStudy getBy(Long id) {
        return repository.findById(id)
                .orElseThrow(NightStudyNotFoundException::new);
    }

    public boolean checkDurationDuplication(Student student, LocalDate startAt, LocalDate endAt) {
        return repository.existsValidByStudentAndDate(student, startAt, endAt);
    }

    public List<NightStudy> getMy(Student student, LocalDate now) {
        return repository.findByStudentAndEndAtGreaterThanEqual(student, now);
    }

    public List<NightStudy> getPending() {
        return repository.findByStatus(NightStudyStatus.PENDING);
    }

    public List<NightStudy> getValid(LocalDate now) {
        return repository.findAllowedStudyByDate(now, NightStudyStatus.ALLOWED);
    }

}