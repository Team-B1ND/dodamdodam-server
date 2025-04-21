package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NightStudyProjectRepository extends JpaRepository<NightStudyProject, Long> {

    Optional<NightStudyProject> findByLeader(Student leader);

    List<NightStudyProject> findByStartAtLessThanEqualAndEndAtGreaterThanEqual(LocalDate today1, LocalDate today2);
}
