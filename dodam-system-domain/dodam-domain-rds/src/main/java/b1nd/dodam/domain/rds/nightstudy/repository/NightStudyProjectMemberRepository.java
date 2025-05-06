package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudy;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NightStudyProjectMemberRepository extends JpaRepository<NightStudyProjectMember, Long> {

    Optional<NightStudyProjectMember> findByStudentAndProject(Student student, NightStudyProject project);

    default boolean existsValidByStudentAndDate(Student student, LocalDate startAt, LocalDate endAt) {
        return findValidStudyByStudentAndDate(
                student, startAt, endAt, ApprovalStatus.REJECTED
        ).isPresent();
    }

    @Query("select m from NightStudyProjectMember m " +
            "join m.project p " +
            "where m.student = :student and " +
            "(:startAt between p.startAt and p.endAt or :endAt between p.startAt and p.endAt) and " +
            "p.status <> :status"
    )
    Optional<NightStudyProjectMember> findValidStudyByStudentAndDate(@Param("student") Student student,
                                                    @Param("startAt") LocalDate startAt,
                                                    @Param("endAt") LocalDate endAt,
                                                    @Param("status") ApprovalStatus status);

    List<NightStudyProjectMember> findAllByProject(NightStudyProject project);
}
