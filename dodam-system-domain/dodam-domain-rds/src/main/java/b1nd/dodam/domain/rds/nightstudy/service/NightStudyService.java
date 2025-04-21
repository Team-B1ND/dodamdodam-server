package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyType;
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

    public boolean checkDurationDuplication(Student student, LocalDate startAt, LocalDate endAt, NightStudyType type) {
        return repository.existsValidByStudentAndDate(student, startAt, endAt, type);
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

    public List<NightStudy> getAllByProject(Long projectId) {
        return repository.findByProject_Id(projectId);
    }
}
