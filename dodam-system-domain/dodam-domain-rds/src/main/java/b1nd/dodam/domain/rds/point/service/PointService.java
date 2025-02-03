package b1nd.dodam.domain.rds.point.service;

import b1nd.dodam.domain.rds.member.entity.*;
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
import java.util.Objects;
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
        List<StudentRelation> relations = studentRelationRepository.findAllByStudents(students);

        Map<Student, List<Parent>> studentParentMap = relations.stream()
                .collect(Collectors.groupingBy(
                        StudentRelation::getStudent,
                        Collectors.mapping(StudentRelation::getParent, Collectors.toList())
                ));

        students.forEach(student -> {
            List<Parent> parents = studentParentMap.getOrDefault(student, Collections.emptyList());
            if (!parents.isEmpty()) {
                parents.stream()
                        .filter(Objects::nonNull)
                        .filter(parent -> parent.getMember().isAlarm())
                        .forEach(parent -> eventPublisher.publishEvent(
                                PointMessageUtil.createIssuedEvent(student, parent, reason)
                        ));
            }
        });
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

        if (parents != null && !parents.isEmpty()) {
            parents.forEach(parent ->
                    eventPublisher.publishEvent(PointMessageUtil.createCanceledEvent(student, parent, reason))
            );
        }
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
