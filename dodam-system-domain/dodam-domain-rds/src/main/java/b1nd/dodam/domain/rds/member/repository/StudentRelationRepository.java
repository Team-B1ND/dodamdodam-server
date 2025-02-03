package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.StudentRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRelationRepository extends JpaRepository<StudentRelation, Integer> {

    @Query("select sr from student_relation sr where sr.student in :students")
    List<StudentRelation> findAllByStudents(@Param("students") List<Student> students);

    @Query("select sr from student_relation sr where sr.student = :student")
    List<Parent> findParentByStudent(@Param("student") Student student);
}
