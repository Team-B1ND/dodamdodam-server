package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyProjectRepository extends JpaRepository<NightStudyProject, Long> {

    List<NightStudyProject> findByLeaderAndStartAtLessThanEqualAndEndAtGreaterThanEqual(Student leader, LocalDate today1, LocalDate today2);

    List<NightStudyProject> findByStartAtLessThanEqualAndEndAtGreaterThanEqual(LocalDate today1, LocalDate today2);

    List<NightStudyProject> findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(ApprovalStatus status, LocalDate today1, LocalDate today2);

    List<NightStudyProject> findByStatusAndEndAtGreaterThanEqual(ApprovalStatus status, LocalDate today);

    List<NightStudyProject> findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqualOrderByRoom(ApprovalStatus status, LocalDate today1, LocalDate today2);

}
