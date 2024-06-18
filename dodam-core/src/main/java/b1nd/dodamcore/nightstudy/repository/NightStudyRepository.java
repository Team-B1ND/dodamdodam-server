package b1nd.dodamcore.nightstudy.repository;

import b1nd.dodamcore.member.domain.entity.Student;
import b1nd.dodamcore.nightstudy.domain.entity.NightStudy;
import b1nd.dodamcore.nightstudy.domain.enums.NightStudyStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;

@Repository
public interface NightStudyRepository extends JpaRepository<NightStudy, Long> {

    default boolean existsValidByStudentAndDate(Student student, LocalDate startAt, LocalDate endAt) {
        return findValidStudyByStudentAndDate(
                student, startAt, endAt, NightStudyStatus.REJECTED, PageRequest.of(0, 1)
        ).size() != 0;
    }

    @Query("select n from NightStudy n " +
            "where n.student = :student and " +
            "(:startAt between n.startAt and n.endAt or :endAt between n.startAt and n.endAt) " +
            "and n.status <> :status")
    List<NightStudy> findValidStudyByStudentAndDate(@Param("student") Student student,
                                              @Param("startAt") LocalDate startAt,
                                              @Param("endAt") LocalDate endAt,
                                              @Param("status") NightStudyStatus status,
                                              Pageable pageable);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDate now);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByStatus(NightStudyStatus status);

    @EntityGraph(attributePaths = {"student.member"})
    @Query("select n from NightStudy n join n.student s join s.member m " +
            "where n.endAt >= :now and n.startAt <= :now and n.status = :status " +
            "order by n.student.grade, n.student.room, n.student.number")
    List<NightStudy> findAllowedStudyByDate(@Param("now") LocalDate now, @Param("status") NightStudyStatus status);

}