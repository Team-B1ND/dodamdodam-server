package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyBannedStudentException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyBanService {

    private final NightStudyBanRepository repository;

    public void save(NightStudyBan nightStudyBan) {
        repository.save(nightStudyBan);
    }

    public void delete(NightStudyBan nightStudyBan) {
        repository.delete(nightStudyBan);
    }

    public List<NightStudyBan> getAllActiveBans() {
        return repository.findActiveBans(LocalDate.now());
    }

    public void existUserBan(Student student) {
        LocalDate today = LocalDate.now();
        NightStudyBan ban = repository.findByStudent(student);
        if (ban == null) return;
        if (today.isBefore(ban.getStarted()) || today.isAfter(ban.getEnded())) return;
        throw new NightStudyBannedStudentException();
    }
}
