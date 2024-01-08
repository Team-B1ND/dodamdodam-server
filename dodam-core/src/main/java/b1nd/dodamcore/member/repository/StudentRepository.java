package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface StudentRepository extends JpaRepository<Student, Integer> {

    @Query(value = "select * from student as a " +
            "join member as b on a.fk_member_id = b.id " +
            "join classroom as c on a.fk_classroom_id = c.id " +
            "where b.status = 'ACTIVE' order by a.id",
            nativeQuery = true)
    List<Student> findStudentByAllowed();

    @Query(value = "select * from student as a " +
            "join member as b on a.fk_member_id = b.id " +
            "join classroom as c on a.fk_classroom_id = c.id " +
            "order by c.id",
            nativeQuery = true)
    List<Student> findStudentByClassroom();

    void deleteByMember(Member member);

    Optional<Student> findByMember(Member member);

    List<Student> findStudentByClassroom_Grade(int classroom_grade);

    @Query(value = "select * from student as a " +
            "join member as b on a.fk_member_id = b.id " +
            "join classroom as c on a.fk_classroom_id = c.id " +
            "where b.status = 'DEACTIVATED' order by a.id",
            nativeQuery = true)
    List<Student> findStudentByNotAllowed();
}
