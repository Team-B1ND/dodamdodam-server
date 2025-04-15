package b1nd.dodam.domain.rds.nightstudy.service;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyBanNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyBannedStudentException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class NightStudyBanService {

    private final NightStudyBanRepository repository;

    public void updateBan(Student student, String reason, LocalDate today, LocalDate ended) {
        NightStudyBan ban = repository.findByStudent(student)
            .orElse(createBan(student));
        ban.updateInfo(reason, today, ended);
        repository.save(ban);
    }

    private NightStudyBan createBan(Student student) {
        return NightStudyBan.builder()
            .student(student)
            .build();
    }

    public void validateBan(Student student) {
        NightStudyBan ban = repository.findByStudentAndEndedGreaterThanEqual(student, LocalDate.now()).orElse(null);
        if (ban != null) throw new NightStudyBannedStudentException();
    }

    public void save(NightStudyBan nightStudyBan) {
        repository.save(nightStudyBan);
    }

    public void delete(NightStudyBan nightStudyBan) {
        repository.delete(nightStudyBan);
    }

    public NightStudyBan findByStudent(Student student) {
        return repository.findByStudentAndEndedGreaterThanEqual(student, LocalDate.now())
            .orElse(null);
    }

    public List<NightStudyBan> getAllActiveBans() {
        return repository.findByEndedGreaterThanEqual(LocalDate.now());
    }

    public NightStudyBan findUserBan(Student student) {
        return repository.findByStudentAndEndedGreaterThanEqual(student, LocalDate.now())
            .orElseThrow(NightStudyBanNotFoundException::new);
    }
}
