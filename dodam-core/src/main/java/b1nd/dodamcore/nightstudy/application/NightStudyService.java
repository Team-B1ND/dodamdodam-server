package b1nd.dodamcore.nightstudy.application;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.exception.StudentNotFoundException;
import b1nd.dodamcore.member.domain.exception.TeacherNotFoundException;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import b1nd.dodamcore.nightstudy.application.dto.req.ApplyNightStudyReq;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import b1nd.dodamcore.nightstudy.domain.exception.NightStudyDuplicateException;
import b1nd.dodamcore.nightstudy.domain.exception.NightStudyNotFoundException;
import b1nd.dodamcore.nightstudy.domain.vo.NightStudyVo;
import b1nd.dodamcore.nightstudy.repository.NightStudyRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class NightStudyService {

    private final NightStudyRepository nightStudyRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberSessionHolder memberSessionHolder;

    @Transactional
    public void apply(ApplyNightStudyReq req) {
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(StudentNotFoundException::new);
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        if(nightStudyRepository.existsByStudentAndStatusNotAndStartAtLessThanEqualAndEndAtGreaterThanEqual
                (student, NightStudyStatus.REJECTED, now, now)
        ) {
            throw new NightStudyDuplicateException();
        }

        nightStudyRepository.save(req.toEntity(student));
    }

    @Transactional
    public void cancel(Long id) {
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(StudentNotFoundException::new);
        NightStudy nightStudy = nightStudyRepository.findById(id)
                .orElseThrow(NightStudyNotFoundException::new);

        nightStudy.isApplicant(student);

        nightStudyRepository.delete(nightStudy);
    }

    @Transactional
    public void modifyStatus(Long id, NightStudyStatus status) {
        NightStudy nightStudy = nightStudyRepository.findById(id)
                .orElseThrow(NightStudyNotFoundException::new);
        Teacher teacher = teacherRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(TeacherNotFoundException::new);

        nightStudy.modifyStatus(teacher, status);
    }

    @Transactional(readOnly = true)
    public List<NightStudyVo> getMy() {
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(StudentNotFoundException::new);
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        return nightStudyRepository.findAllByStudentAndEndAtGreaterThanEqual(student, now).stream()
                .map(NightStudyVo::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NightStudyVo> getPending() {
        return nightStudyRepository.findAllByStatus(NightStudyStatus.PENDING).stream()
                .map(NightStudyVo::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<NightStudyVo> getValid() {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        return nightStudyRepository.findValidStudyByDate(now, NightStudyStatus.ALLOWED).stream()
                .map(NightStudyVo::of)
                .toList();
    }

}