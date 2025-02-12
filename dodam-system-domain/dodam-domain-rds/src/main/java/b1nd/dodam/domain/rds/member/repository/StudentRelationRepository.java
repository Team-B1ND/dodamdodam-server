package b1nd.dodam.domain.rds.member.repository;

import b1nd.dodam.domain.rds.member.entity.Parent;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.entity.StudentRelation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface StudentRelationRepository extends JpaRepository<StudentRelation, Integer> {

    List<Parent> findByStudentAndParentMemberIsAlarmTrue(Student student);

    @Query("select sr.student, sr.parent from student_relation sr " +
            "where sr.student in :students and sr.parent.member.isAlarm = true and sr.student.member.isAlarm = true")
    List<Object[]> findParentsAndStudentsWithAlarmByStudents(@Param("students") List<Student> students);

}
