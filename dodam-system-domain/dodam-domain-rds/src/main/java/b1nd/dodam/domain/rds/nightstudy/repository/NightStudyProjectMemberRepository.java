package b1nd.dodam.domain.rds.nightstudy.repository;

import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProject;
import b1nd.dodam.domain.rds.nightstudy.entity.NightStudyProjectMember;
import b1nd.dodam.domain.rds.nightstudy.enumeration.NightStudyProjectType;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

public interface NightStudyProjectMemberRepository extends JpaRepository<NightStudyProjectMember, Long> {

    Optional<NightStudyProjectMember> findByStudentAndProject(Student student, NightStudyProject project);

    default boolean existsValidByStudentAndDate(List<Student> students, LocalDate startAt, LocalDate endAt, NightStudyProjectType type) {
        return !findValidStudyByStudentAndDate(students, startAt, endAt, type, ApprovalStatus.REJECTED).isEmpty();
    }

    @Query("""
        select m from NightStudyProjectMember m
        join m.project p
        where m.student in :students and
        (:startAt between p.startAt and p.endAt or :endAt between p.startAt and p.endAt) and
        p.type = :type and
        p.status <> :status
    """)
    List<NightStudyProjectMember> findValidStudyByStudentAndDate(
        @Param("students") List<Student> students,
        @Param("startAt") LocalDate startAt,
        @Param("endAt") LocalDate endAt,
        @Param("type") NightStudyProjectType type,
        @Param("status") ApprovalStatus status
    );

    List<NightStudyProjectMember> findAllByProject(NightStudyProject project);

    List<NightStudyProjectMember> findByStudentAndProject_EndAtGreaterThanEqual(Student student, LocalDate now);

    @Query("""
    select m from NightStudyProjectMember m
    join m.project p
    where p.status = 'ALLOWED' and
        :today between p.startAt and p.endAt
""")
    List<NightStudyProjectMember> findAllowedProjectMembers(
            @Param("today") LocalDate today
    );

    @Query("""
        SELECT m FROM NightStudyProjectMember m
        JOIN FETCH m.project p
        JOIN FETCH m.student s
        WHERE p.status = :status AND p.endAt >= :date
        ORDER BY p.id, m.role
    """)
    List<NightStudyProjectMember> findMemberWithProjectByStatus(@Param("status") ApprovalStatus status, @Param("date") LocalDate date);
}
