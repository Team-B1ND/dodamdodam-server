package b1nd.dodamcore.point.repository;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.point.domain.entity.Point;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface PointRepository extends JpaRepository<Point, Integer> {

    @EntityGraph(attributePaths = {"reason"})
    List<Point> findByStudent(Student student);

}