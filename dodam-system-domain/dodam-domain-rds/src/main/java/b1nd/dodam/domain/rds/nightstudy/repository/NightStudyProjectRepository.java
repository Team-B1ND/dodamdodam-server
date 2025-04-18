package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyProjectRepository extends JpaRepository<NightStudyProject, Long> {

//    default boolean existsValidByStudentAndDate(List<Integer> students, LocalDate startAt, LocalDate endAt) {
//        return findValidStudentByStudentAndDate(students, startAt, endAt, ApprovalStatus.REJECTED);
//    }

    @Query("""
        SELECT n
        FROM NightStudyProject n
        WHERE n.startAt <= :endDate AND n.endAt >= :startDate
    """)
    List<NightStudyProject> findAllByDateRange(@Param("startDate") LocalDate startDate, @Param("endDate") LocalDate endDate);
}
