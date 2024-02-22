package b1nd.dodamcore.outsleeping.application;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import b1nd.dodamcore.outsleeping.application.dto.req.ApplyOutSleepingReq;
import b1nd.dodamcore.outsleeping.application.dto.res.OutSleepingRes;
import b1nd.dodamcore.outsleeping.domain.entity.OutSleeping;
import b1nd.dodamcore.outsleeping.domain.enums.OutSleepingStatus;
import b1nd.dodamcore.outsleeping.repository.OutSleepingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutSleepingService {

    private final OutSleepingRepository outSleepingRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberSessionHolder memberSessionHolder;

    @Transactional
    public void apply(ApplyOutSleepingReq req) {
        Student student = studentRepository.getByMember(memberSessionHolder.current());
        OutSleeping outSleeping = req.toEntity(student);

        outSleepingRepository.save(outSleeping);
    }

    @Transactional
    public void modifyStatus(Long id, OutSleepingStatus status) {
        Teacher teacher = teacherRepository.getByMember(memberSessionHolder.current());
        OutSleeping outSleeping = outSleepingRepository.getById(id);

        outSleeping.modifyStatus(teacher, status);
    }

    @Transactional
    public void cancel(Long id) {
        Student student = studentRepository.getByMember(memberSessionHolder.current());
        OutSleeping outSleeping = outSleepingRepository.getById(id);

        outSleeping.isApplicant(student);

        outSleepingRepository.delete(outSleeping);
    }

    @Transactional(readOnly = true)
    public List<OutSleepingRes> getValid() {
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        return getByDate(now).stream()
                .filter(o -> OutSleepingStatus.ALLOWED.equals(o.status()))
                .toList();
    }

    @Transactional(readOnly = true)
    public List<OutSleepingRes> getByDate(LocalDate date) {
        return OutSleepingRes.of(
                outSleepingRepository.findByDate(date)
        );
    }

    @Transactional(readOnly = true)
    public List<OutSleepingRes> getMy() {
        Student student = studentRepository.getByMember(memberSessionHolder.current());
        LocalDate now = ZonedDateTimeUtil.nowToLocalDate();

        return OutSleepingRes.of(
                outSleepingRepository.findByStudentAndEndAtGreaterThanEqual(student, now)
        );
    }

}