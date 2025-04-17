package b1nd.dodam.domain.rds.nightstudy.repository;

import java.util.List;
import java.time.LocalDate;
import java.util.Optional;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface NightStudyBanRepository extends JpaRepository<NightStudyBan, Long> {

    Optional<NightStudyBan> findByStudent(Student student);
    
    Optional<NightStudyBan> findByStudentAndEndedGreaterThanEqual(Student student, LocalDate today);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudyBan> findByEndedGreaterThanEqual(LocalDate today);

    @Override
    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudyBan> findAll();
}
