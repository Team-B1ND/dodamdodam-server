package b1nd.dodam.domain.rds.nightstudy.service;

import b1nd.dodam.core.util.ZonedDateTimeUtil;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyBanNotFoundException;
import b1nd.dodam.domain.rds.nightstudy.exception.NightStudyBannedStudentException;
import b1nd.dodam.domain.rds.nightstudy.repository.NightStudyBanRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyBanService {
    private final StudentRepository studentRepository;
    private final NightStudyBanRepository nightStudyBanRepository;

    public void updateBan(Student student, String reason, LocalDate today, LocalDate ended) {
        NightStudyBan ban = nightStudyBanRepository.findByStudent(student)
            .orElse(createBan(student));
        ban.updateInfo(reason, today, ended);
        nightStudyBanRepository.save(ban);
    }

    private NightStudyBan createBan(Student student) {
        return NightStudyBan.builder()
            .student(student)
            .build();
    }

    public void validateBan(Student student) {
        NightStudyBan ban = nightStudyBanRepository.findByStudentAndEndedGreaterThanEqual(student, ZonedDateTimeUtil.nowToLocalDate()).orElse(null);
        if (ban != null) throw new NightStudyBannedStudentException();
    }

    public void validateBan(Integer studentId) {
        Student student = studentRepository.getById(studentId);
        NightStudyBan ban = nightStudyBanRepository.findByStudentAndEndedGreaterThanEqual(student, ZonedDateTimeUtil.nowToLocalDate()).orElse(null);
        if (ban != null) throw new NightStudyBannedStudentException();
    }

    public void save(NightStudyBan nightStudyBan) {
        nightStudyBanRepository.save(nightStudyBan);
    }

    public void delete(NightStudyBan nightStudyBan) {
        nightStudyBanRepository.delete(nightStudyBan);
    }

    public NightStudyBan findByStudent(Student student) {
        return nightStudyBanRepository.findByStudentAndEndedGreaterThanEqual(student, ZonedDateTimeUtil.nowToLocalDate())
            .orElse(null);
    }

    public List<NightStudyBan> getAllActiveBans() {
        return nightStudyBanRepository.findByEndedGreaterThanEqual(ZonedDateTimeUtil.nowToLocalDate());
    }

    public NightStudyBan findUserBan(Student student) {
        return nightStudyBanRepository.findByStudentAndEndedGreaterThanEqual(student, ZonedDateTimeUtil.nowToLocalDate())
            .orElseThrow(NightStudyBanNotFoundException::new);
    }

    public List<Integer> findAllStudentIdByDate(LocalDate date) {
        return nightStudyBanRepository.findByEndedGreaterThanEqual(date)
                .stream()
                .map(NightStudyBan::getStudentId)
                .toList();
    }
}
