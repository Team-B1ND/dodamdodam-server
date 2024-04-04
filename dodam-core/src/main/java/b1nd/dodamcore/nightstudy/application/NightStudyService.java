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
import b1nd.dodamcore.nightstudy.application.dto.res.NightStudyRes;
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

        if(nightStudyRepository.existsValidByStudentAndDate(student, req.startAt(), req.endAt())) {
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
    public void modifyStatus(Long id, NightStudyStatus status, String rejectReason) {
        NightStudy nightStudy = nightStudyRepository.findById(id)
                .orElseThrow(NightStudyNotFoundException::new);
        Teacher teacher = teacherRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(TeacherNotFoundException::new);

        nightStudy.modifyStatus(teacher, status, rejectReason);
    }

    @Transactional(readOnly = true)
    public List<NightStudyRes> getMy() {
        Student student = studentRepository.findByMember(memberSessionHolder.current())
                .orElseThrow(StudentNotFoundException::new);
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        return NightStudyRes.of(
                nightStudyRepository.findByStudentAndEndAtGreaterThanEqual(student, now)
        );
    }

    @Transactional(readOnly = true)
    public List<NightStudyRes> getPending() {
        return NightStudyRes.of(
                nightStudyRepository.findByStatus(NightStudyStatus.PENDING)
        );
    }

    @Transactional(readOnly = true)
    public List<NightStudyRes> getValid() {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        return NightStudyRes.of(
                nightStudyRepository.findValidStudyByDate(now, NightStudyStatus.ALLOWED)
        );
    }

}