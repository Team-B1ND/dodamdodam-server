package b1nd.dodamcore.nightstudy.repository;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NightStudyRepository extends JpaRepository<NightStudy, Long> {

    boolean existsByStudentAndStatusNotAndStartAtLessThanEqualAndEndAtGreaterThanEqual(Student student, NightStudyStatus status, LocalDate now, LocalDate now2);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudy> findAllByStudentAndEndAtGreaterThanEqual(Student student, LocalDate now);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudy> findAllByStatus(NightStudyStatus status);

    @EntityGraph(attributePaths = {"student", "student.member"})
    @Query("select n from NightStudy n where n.endAt >= :now and n.startAt <= :now and n.status = :status")
    List<NightStudy> findValidStudyByDate(@Param("now") LocalDate now, @Param("status") NightStudyStatus status);

}