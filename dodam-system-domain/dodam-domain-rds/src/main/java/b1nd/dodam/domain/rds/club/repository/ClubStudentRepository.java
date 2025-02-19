package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ClubStudentRepository extends JpaRepository<ClubMember, Long> {
    List<ClubMember> findAllByStudentAndPermissionAndClub_Type(Student student, ClubPermission permission, ClubType clubType);

    boolean existsByClubAndStudentAndPermission(Club club, Student student, ClubPermission permission);

    @Query("""
    SELECT cs FROM club_member cs
    WHERE cs.student IN :students
    AND (:permission IS NULL OR cs.permission = :permission)
    AND cs.clubStatus = :status
    AND cs.club.type = :type
    AND cs.club.state <> :state
    """)
    List<ClubMember> findByStudentInAndPermissionAndClubStatusAndClub_TypeAndClub_StateNot(
//            List<Student> student, ClubPermission permission, ClubStatus clubStatus, ClubType clubType, ClubStatus state
            @Param("students") List<Student> students,
            @Param("permission") ClubPermission permission,
            @Param("status") ClubStatus status,
            @Param("type") ClubType type,
            @Param("state") ClubStatus state
    );
}
