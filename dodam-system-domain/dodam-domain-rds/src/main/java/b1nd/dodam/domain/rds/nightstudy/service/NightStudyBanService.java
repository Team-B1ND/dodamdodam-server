package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
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

    public NightStudyBan findByStudent(Student student) {
        return repository.findByStudent(student);
    }

    public List<NightStudyBan> getAllActiveBans() {
        return repository.findByEndedGreaterThanEqual(LocalDate.now());
    }

    public NightStudyBan findUserBan(Student student) {
        return repository.findByStudent(student);
    }

    public boolean checkBanDuplication(Student student) {
        return repository.findByStudent(student) != null;
    }
}
