package b1nd.dodamcore.point.application;

import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.member.domain.entity.Teacher;
import b1nd.dodamcore.member.domain.event.StudentRegisteredEvent;
import b1nd.dodamcore.member.domain.exception.StudentNotFoundException;
import b1nd.dodamcore.member.repository.StudentRepository;
import b1nd.dodamcore.member.repository.TeacherRepository;
import b1nd.dodamcore.point.application.dto.req.IssuePointReq;
import b1nd.dodamcore.point.application.dto.res.PointRes;
import b1nd.dodamcore.point.application.dto.res.PointScoreRes;
import b1nd.dodamcore.point.domain.entity.Point;
import b1nd.dodamcore.point.domain.entity.PointReason;
import b1nd.dodamcore.point.domain.entity.PointScore;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.event.PointCanceledEvent;
import b1nd.dodamcore.point.domain.event.PointIssuedEvent;
import b1nd.dodamcore.point.domain.exception.PointNotFoundException;
import b1nd.dodamcore.point.domain.exception.PointReasonNotFoundException;
import b1nd.dodamcore.point.domain.exception.PointScoreNotFoundException;
import b1nd.dodamcore.point.repository.PointReasonRepository;
import b1nd.dodamcore.point.repository.PointRepository;
import b1nd.dodamcore.point.repository.PointScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.event.TransactionalEventListener;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointReasonRepository reasonRepository;
    private final PointScoreRepository scoreRepository;
    private final StudentRepository studentRepository;
    private final TeacherRepository teacherRepository;
    private final MemberSessionHolder memberSessionHolder;
    private final ApplicationEventPublisher applicationEventPublisher;

    @Transactional
    public void issue(IssuePointReq req) {
        Teacher teacher = teacherRepository.getByMember(memberSessionHolder.current());
        PointReason reason = reasonRepository.findById(req.reasonId())
                .orElseThrow(PointReasonNotFoundException::new);
        List<Student> students = studentRepository.getByIds(req.studentIds());

        pointRepository.saveAll(
                students.stream()
                        .map(s -> Point.builder()
                                .student(s)
                                .teacher(teacher)
                                .reason(reason)
                                .issueAt(req.issueAt())
                                .build()
                        )
                        .toList()
        );

        scoreRepository.getByStudentIn(students).forEach(s -> s.issue(reason));

        publishIssuedEvent(students, reason);
    }

    @Transactional
    public void cancel(int id) {
        Point point = pointRepository.findById(id)
                .orElseThrow(PointNotFoundException::new);
        PointScore score = scoreRepository.findByStudent(point.getStudent())
                .orElseThrow(PointScoreNotFoundException::new);

        score.cancel(point.getReason());

        publishCanceledEvent(point.getReason(), point.getStudent().getMember());

        pointRepository.delete(point);
    }

    @Transactional(readOnly = true)
    public List<PointRes> getMyByPointType(PointType type) {
        Student student = studentRepository.getByMember(memberSessionHolder.current());

        return pointRepository.findByStudent(student).stream()
                .filter(p -> type == p.getReason().getPointType())
                .map(PointRes::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public List<PointRes> getByStudentAndPointType(int studentId, PointType type) {
        Student student = studentRepository.findById(studentId)
                .orElseThrow(StudentNotFoundException::new);

        return pointRepository.findByStudent(student).stream()
                .filter(p -> type == p.getReason().getPointType())
                .map(PointRes::of)
                .toList();
    }

    @Transactional(readOnly = true)
    public PointScoreRes getMyScoreByPointType(PointType type) {
        Student student = studentRepository.getByMember(memberSessionHolder.current());
        PointScore score = scoreRepository.findByStudent(student)
                .orElseThrow(PointScoreNotFoundException::new);

        return PointScoreRes.of(score, type);
    }

    @Transactional(readOnly = true)
    public List<PointScoreRes> getAllScoreByPointType(PointType type) {
        return scoreRepository.findAll().stream()
                .map(p -> PointScoreRes.of(p, type))
                .toList();
    }

    @Async
    @Transactional(propagation = Propagation.REQUIRES_NEW)
    @TransactionalEventListener
    public void listen(StudentRegisteredEvent e) {
        scoreRepository.save(PointScore.builder()
                .student(e.student())
                .build());
    }

    private void publishIssuedEvent(List<Student> students, PointReason reason) {
        students.forEach(
                student -> applicationEventPublisher.publishEvent(
                        new PointIssuedEvent(
                                PointSMSHelper.getMessage(reason, student.getMember().getName(), "발급"),
                                student.getMember().getPhone()
                        )
                )
        );
    }

    private void publishCanceledEvent(PointReason reason, Member member) {
        applicationEventPublisher.publishEvent(
                new PointCanceledEvent(
                        PointSMSHelper.getMessage(reason, member.getName(), "취소"),
                        member.getPhone()
                )
        );
    }

}