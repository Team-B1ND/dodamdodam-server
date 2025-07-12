package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyRepository extends JpaRepository<NightStudy, Long> {

    default boolean existsValidByStudentAndDate(Student student, LocalDate startAt, LocalDate endAt) {
        return !findValidStudyByStudentAndDate(
                student, startAt, endAt, ApprovalStatus.REJECTED, PageRequest.of(0, 1)
        ).isEmpty();
    }

    @Query("select n from NightStudy n " +
            "where n.student = :student and " +
            "(:startAt between n.startAt and n.endAt or :endAt between n.startAt and n.endAt) and " +
            "n.status <> :status")
    List<NightStudy> findValidStudyByStudentAndDate(@Param("student") Student student,
                                              @Param("startAt") LocalDate startAt,
                                              @Param("endAt") LocalDate endAt,
                                              @Param("status") ApprovalStatus status,
                                              Pageable pageable);

    @Query("SELECT COUNT(n) > 0 FROM NightStudy n " +
            "WHERE n.student IN :students AND " +
            "n.status IN ('ALLOWED', 'PENDING') AND " +
            "n.endAt >= :now")
    boolean existsActiveNightStudy(@Param("students") List<Student> students,
                                   @Param("now") LocalDate now);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDate now);

    @Query("select n from NightStudy n join fetch n.student s join fetch n.student.member " +
            "where n.endAt >= :now and n.startAt <= :now " +
            "order by s.grade, s.room, s.number")
    List<NightStudy> findAllByDate(@Param("now") LocalDate now);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByStatus(ApprovalStatus status);

    @Query("select n from NightStudy n join fetch n.student s join fetch n.student.member " +
            "where n.endAt >= :now and n.startAt <= :now and n.status = :status " +
            "order by s.grade, s.room, s.number")
    List<NightStudy> findAllowedStudyByDate(@Param("now") LocalDate now, @Param("status") ApprovalStatus status);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByEndAt(LocalDate endAt);

    @Query("SELECT n FROM NightStudy n " +
            "JOIN FETCH n.student s " +
            "JOIN FETCH s.member " +
            "WHERE n.status = 'ALLOWED' AND n.endAt >= :date AND n.startAt <= :date " +
            "ORDER BY s.grade, s.room, s.number")
    List<NightStudy> findCombinedNightStudy(@Param("date") LocalDate date);
}
