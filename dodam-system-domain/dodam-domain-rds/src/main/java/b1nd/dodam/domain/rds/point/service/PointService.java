package b1nd.dodam.domain.rds.point.service;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.point.entity.Point;
import b1nd.dodam.domain.rds.point.entity.PointScore;
import b1nd.dodam.domain.rds.point.enumeration.PointType;
import b1nd.dodam.domain.rds.point.exception.PointNotFoundException;
import b1nd.dodam.domain.rds.point.exception.PointScoreNotFoundException;
import b1nd.dodam.domain.rds.point.repository.PointRepository;
import b1nd.dodam.domain.rds.point.repository.PointScoreRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class PointService {

    private final PointRepository pointRepository;
    private final PointScoreRepository scoreRepository;

    public void save(List<Point> points) {
        pointRepository.saveAll(points);
    }

    public void save(PointScore score) {
        scoreRepository.save(score);
    }

    public void delete(Point point) {
        pointRepository.delete(point);
    }

    public Point getPointBy(Integer id) {
        return pointRepository.findById(id)
                .orElseThrow(PointNotFoundException::new);
    }

    public List<Point> getPointsBy(Student student, PointType type) {
        return pointRepository.findByStudent(student).parallelStream()
                .filter(p -> type == p.getReason().getPointType())
                .toList();
    }

    public PointScore getScoreBy(Student student) {
        return scoreRepository.findByStudent(student)
                .orElseThrow(PointScoreNotFoundException::new);
    }

    public PointScore getScoreByStudentForUpdate(Student student) {
        return scoreRepository.findByStudentWithPessimisticLock(student)
                .orElseThrow(PointScoreNotFoundException::new);
    }

    public List<PointScore> getScoresByStudentsForUpdate(List<Student> students) {
        return scoreRepository.getByStudentInWithPessimisticLock(students);
    }

    public List<PointScore> getAllScores() {
        return scoreRepository.findAll();
    }

}
