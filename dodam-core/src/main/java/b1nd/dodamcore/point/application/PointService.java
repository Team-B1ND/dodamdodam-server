package b1nd.dodamcore.point.application;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.point.domain.entity.Point;
import b1nd.dodamcore.point.domain.entity.PointScore;
import b1nd.dodamcore.point.domain.enums.PointType;
import b1nd.dodamcore.point.domain.exception.PointNotFoundException;
import b1nd.dodamcore.point.domain.exception.PointScoreNotFoundException;
import b1nd.dodamcore.point.repository.PointRepository;
import b1nd.dodamcore.point.repository.PointScoreRepository;
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

    public List<PointScore> getScoresBy(List<Student> students) {
        return scoreRepository.getByStudentIn(students);
    }

    public List<PointScore> getAllScores() {
        return scoreRepository.findAll();
    }

}
