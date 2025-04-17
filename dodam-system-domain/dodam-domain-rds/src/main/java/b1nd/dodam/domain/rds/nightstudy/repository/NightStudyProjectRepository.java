package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyProjectRepository extends JpaRepository<NightStudyProject, Long> {

    default boolean existsValidByStudentAndDate(List<Integer> students, LocalDate startAt, LocalDate endAt) {
        return findValidStudentByStudentAndDate(students, startAt, endAt, ApprovalStatus.REJECTED);
    }

    @Query("SELECT n from NightStudyProject n" +)
            "WHERE "
}
