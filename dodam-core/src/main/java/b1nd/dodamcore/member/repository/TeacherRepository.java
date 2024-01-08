package b1nd.dodamcore.member.repository;

import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.entity.Teacher;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TeacherRepository extends JpaRepository<Teacher, Integer> {

    @Query(value = "select * from teacher as a " +
            "join member as b on a.fk_member_id = b.id " +
            "where b.status = 'ACTIVE' order by a.id",
            nativeQuery = true)
    List<Teacher> findTeacherByAllowed();

    Optional<Teacher> findById(int id);

    Optional<Teacher> findByMember(Member member);

    @Query(value = "select * from teacher as a " +
            "join member as b on a.fk_member_id = b.id " +
            "where b.status = 'ACTIVE' order by a.id",
            nativeQuery = true)
    List<Teacher> findTeacherByNotAllowed();

    void deleteByMember(Member member);
}
