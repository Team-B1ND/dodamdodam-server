package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyBanRepository extends JpaRepository<NightStudyBan, Long> {

    NightStudyBan findByStudent(Student student);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudyBan> findByEndedGreaterThanEqual(LocalDate today);
}
