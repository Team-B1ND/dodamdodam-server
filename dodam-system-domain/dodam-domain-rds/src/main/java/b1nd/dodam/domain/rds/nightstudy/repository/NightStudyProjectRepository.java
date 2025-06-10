package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyProjectRepository extends JpaRepository<NightStudyProject, Long> {

    List<NightStudyProject> findByStartAtLessThanEqualAndEndAtGreaterThanEqual(LocalDate today1, LocalDate today2);

    List<NightStudyProject> findByStatusNotAndStartAtLessThanEqualAndEndAtGreaterThanEqualOrderByRoom(ApprovalStatus status, LocalDate today1, LocalDate today2);

}
