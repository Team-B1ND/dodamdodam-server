package b1nd.dodamcore.outgoing.application;

import b1nd.dodamcore.common.util.ZonedDateTimeUtil;
import b1nd.dodamcore.outgoing.application.dto.res.OutGoingRes;
import b1nd.dodamcore.outgoing.domain.enums.OutGoingStatus;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import b1nd.dodamcore.outgoing.application.dto.req.ApplyOutGoingReq;
import b1nd.dodamcore.outgoing.domain.entity.OutGoing;
import b1nd.dodamcore.outgoing.repository.OutGoingRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class OutGoingService {

    private final OutGoingRepository outGoingRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberSessionHolder memberSessionHolder;

    @Transactional
    public void apply(ApplyOutGoingReq req) {
        Student student = studentRepository.getByMember(memberSessionHolder.current());
        OutGoing outGoing = req.toEntity(student);

        outGoingRepository.save(outGoing);
    }

    @Transactional
    public void modifyStatus(Long id, OutGoingStatus status, String rejectReason) {
        Teacher teacher = teacherRepository.getByMember(memberSessionHolder.current());
        OutGoing outGoing = outGoingRepository.getById(id);

        outGoing.modifyStatus(teacher, status, rejectReason);
    }

    @Transactional
    public void cancel(Long id) {
        OutGoing outGoing = outGoingRepository.getById(id);
        Student student = studentRepository.getByMember(memberSessionHolder.current());

        outGoing.isApplicant(student);

        outGoingRepository.delete(outGoing);
    }

    @Transactional(readOnly = true)
    public List<OutGoingRes> getByDate(LocalDate date) {
        LocalDateTime startOfTheDay = LocalDateTime.of(date, LocalTime.MIN);
        LocalDateTime endOfTheDay = LocalDateTime.of(date, LocalTime.MAX);

        return OutGoingRes.of(outGoingRepository.findByDate(startOfTheDay, endOfTheDay));
    }

    @Transactional(readOnly = true)
    public List<OutGoingRes> getMy() {
        Student student = studentRepository.getByMember(memberSessionHolder.current());
        LocalDateTime now = ZonedDateTimeUtil.nowToLocalDateTime();

        return OutGoingRes.of(outGoingRepository.findByStudentAndEndAtGreaterThanEqual(student, now));
    }

}