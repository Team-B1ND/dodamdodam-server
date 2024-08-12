package b1nd.dodam.domain.rds.point.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.point.entity.Point;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PointRepository extends JpaRepository<Point, Integer> {

    @EntityGraph(attributePaths = {"reason", "student.member", "teacher.member"})
    List<Point> findByStudent(Student student);

}
