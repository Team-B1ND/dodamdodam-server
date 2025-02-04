package b1nd.dodam.domain.rds.point.service;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.Teacher;
import b1nd.dodam.domain.rds.member.repository.StudentRelationRepository;
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
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class PointService {

    private final StudentRelationRepository studentRelationRepository;
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

    private void publishPointIssuedEvents(List<Student> students, PointReason reason) {
        List<Object[]> parentsWithAlarm = studentRelationRepository.findParentsAndStudentsWithAlarmByStudents(students);

        Map<Student, List<Parent>> studentParentMap = parentsWithAlarm.stream()
                .collect(Collectors.groupingBy(
                        obj -> (Student)obj[0],
                        Collectors.mapping(obj -> (Parent)obj[1], Collectors.toList())
                ));

        students.forEach(student -> {
            eventPublisher.publishEvent(PointMessageUtil.createIssuedEvent(student, null, reason));

            studentParentMap.getOrDefault(student, Collections.emptyList())
                    .forEach(parent -> eventPublisher.publishEvent(PointMessageUtil.createIssuedEvent(null, parent, reason)));
        });
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

    public void cancel(int pointId) {
        Point point = pointRepository.getById(pointId);
        cancelPointScore(point.getStudent(), point.getReason());
        pointRepository.delete(point);
        publishPointCanceledEvent(point.getStudent(), point.getReason());
    }

    private void cancelPointScore(Student student, PointReason reason) {
        PointScore score = pointScoreRepository.getByStudentForUpdate(student);
        score.cancel(reason);
    }

    private void publishPointCanceledEvent(Student student, PointReason reason) {
        List<Parent> parents = studentRelationRepository.findParentByStudent(student);

        if (student.getMember().isAlarm()) {
            eventPublisher.publishEvent(PointMessageUtil.createCanceledEvent(student, null, reason));
        }

        parents.forEach(parent ->
                eventPublisher.publishEvent(PointMessageUtil.createCanceledEvent(null, parent, reason)));
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

    private boolean isAlarm(Member member) {
        return member != null && member.isAlarm();
    }

}
