package b1nd.dodam.domain.rds.point.service;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.point.entity.Point;
import b1nd.dodam.domain.rds.point.entity.PointReason;
import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.exception.PointScoreNotFoundException;
import b1nd.dodam.domain.rds.point.repository.PointReasonRepository;
import b1nd.dodam.domain.rds.point.repository.PointRepository;
import b1nd.dodam.domain.rds.point.repository.PointScoreRepository;
import b1nd.dodam.domain.rds.point.service.support.PointMessageUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointScoreRepository pointScoreRepository;
    private final PointReasonRepository pointReasonRepository;
    private final ApplicationEventPublisher eventPublisher;

    public void issue(int reasonId, Teacher teacher, List<Student> students, LocalDate issueAt) {
        PointReason reason = pointReasonRepository.getById(reasonId);
        savePoints(teacher, students, reason, issueAt);
        saveScores(students, reason);
        publishPointIssuedEvents(students, reason);
    }

    private void savePoints(Teacher teacher, List<Student> students, PointReason reason, LocalDate issueAt) {
        pointRepository.saveAll(students
                .parallelStream()
                .map(s -> Point.builder()
                        .student(s)
                        .teacher(teacher)
                        .reason(reason)
                        .issueAt(issueAt)
                        .build()
                ).toList()
        );
    }

    private void saveScores(List<Student> students, PointReason reason) {
        pointScoreRepository.getByStudentInForUpdate(students).forEach(
                score -> score.issue(reason)
        );
    }

    private void publishPointIssuedEvents(List<Student> students, PointReason reason) {
        students.forEach(s -> eventPublisher.publishEvent(
                PointMessageUtil.createIssuedEvent(s.getMember(), reason)
        ));
    }

    public void cancel(int pointId) {
        Point point = pointRepository.getById(pointId);
        cancelPointScore(point.getStudent(), point.getReason());
        pointRepository.delete(point);
        publishPointCanceledEvent(point.getReason(), point.getStudent().getMember());
    }

    private void cancelPointScore(Student student, PointReason reason) {
        PointScore score = pointScoreRepository.getByStudentForUpdate(student);
        score.cancel(reason);
    }

    private void publishPointCanceledEvent(PointReason reason, Member member) {
        eventPublisher.publishEvent(PointMessageUtil.createCanceledEvent(member, reason));
    }

    public List<Point> getPointsByStudentAndType(Student student, PointType type) {
        return pointRepository.findByStudent(student)
                .parallelStream()
                .filter(p -> type == p.getReason().getPointType())
                .toList();
    }

    public PointScore getScoreByStudent(Student student) {
        return pointScoreRepository.findByStudent(student)
                .orElseThrow(PointScoreNotFoundException::new);
    }

    public List<PointScore> getAllScores() {
        return pointScoreRepository.findAll();
    }

}
