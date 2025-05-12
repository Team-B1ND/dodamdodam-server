package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NightStudyBanRepository extends JpaRepository<NightStudyBan, Long> {

    Optional<NightStudyBan> findByStudent(Student student);
    
    Optional<NightStudyBan> findByStudentAndEndedGreaterThanEqual(Student student, LocalDate today);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudyBan> findByEndedGreaterThanEqual(LocalDate today);

    List<NightStudyBan> findByStudentInAndEndedGreaterThanEqual(List<Student> students, LocalDate today);

    @Override
    @EntityGraph(attributePaths = {"student", "student.member"})
    List<NightStudyBan> findAll();
}
