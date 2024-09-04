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
        return findValidStudyByStudentAndDate(
                student, startAt, endAt, ApprovalStatus.REJECTED, PageRequest.of(0, 1)
        ).size() != 0;
    }

    @Query("select n from NightStudy n " +
            "where n.student = :student and " +
            "(:startAt between n.startAt and n.endAt or :endAt between n.startAt and n.endAt) " +
            "and n.status <> :status")
    List<NightStudy> findValidStudyByStudentAndDate(@Param("student") Student student,
                                              @Param("startAt") LocalDate startAt,
                                              @Param("endAt") LocalDate endAt,
                                              @Param("status") ApprovalStatus status,
                                              Pageable pageable);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByStudentAndEndAtGreaterThanEqual(Student student, LocalDate now);

    @EntityGraph(attributePaths = {"student.member"})
    List<NightStudy> findByStatus(ApprovalStatus status);

    @EntityGraph(attributePaths = {"student.member"})
    @Query("select n from NightStudy n join n.student s join s.member m " +
            "where n.endAt >= :now and n.startAt <= :now and n.status = :status " +
            "order by n.student.grade, n.student.room, n.student.number")
    List<NightStudy> findAllowedStudyByDate(@Param("now") LocalDate now, @Param("status") ApprovalStatus status);

}