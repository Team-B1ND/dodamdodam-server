package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyDuplicateException;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyRepository;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
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

    public void validateDurationDuplication(Student student, LocalDate startAt, LocalDate endAt) {
        if (repository.existsValidByStudentAndDate(student, startAt, endAt)) throw new NightStudyDuplicateException();
    }

    public void validateNoActiveNightStudies(List<Student> students) {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();
        if (repository.existsActiveNightStudy(students, now)) throw new NightStudyDuplicateException();
    }

    public List<NightStudy> getAll(LocalDate now) {
        return repository.findAllByDate(now);
    }

    public List<NightStudy> getMy(Student student, LocalDate now) {
        return repository.findByStudentAndEndAtGreaterThanEqual(student, now);
    }

    public List<NightStudy> getPending() {
        return repository.findByStatus(ApprovalStatus.PENDING);
    }

    public List<NightStudy> getValid(LocalDate now) {
        return repository.findAllowedStudyByDate(now, ApprovalStatus.ALLOWED);
    }

    public List<NightStudy> getByEndDate(LocalDate endAt){
        return repository.findByEndAt(endAt);
    }

    public void rejectAllByStudent(Student student) {
        repository.findByStudentAndEndAtGreaterThanEqual(student, LocalDate.now())
                .forEach(NightStudy::reject);
    }
}
