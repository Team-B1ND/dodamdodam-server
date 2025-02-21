package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.StudentRelation;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface StudentRelationRepository extends JpaRepository<StudentRelation, Integer> {

    @EntityGraph(attributePaths = {"student"})
    List<StudentRelation> findByParent(Parent parent);
    boolean existsByStudent(Student student);

}
