package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyBan;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;

public interface NightStudyBanRepository extends JpaRepository<NightStudyBan, Long> {

    NightStudyBan findByStudent(Student student);

    @Query("""
        SELECT n
        FROM NightStudyBan n
        WHERE :today BETWEEN n.started AND n.ended
    """)
    List<NightStudyBan> findActiveBans(@Param("today") LocalDate today);
}
