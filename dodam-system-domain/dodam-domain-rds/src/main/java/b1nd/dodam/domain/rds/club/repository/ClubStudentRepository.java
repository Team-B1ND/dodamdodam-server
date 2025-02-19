package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClubStudentRepository extends JpaRepository<ClubMember, Long> {
    List<ClubMember> findAllByStudentAndClub_Type(Student student, ClubType clubType);

    boolean existsByClubAndStudentAndPermission(Club club, Student student, ClubPermission permission);

    boolean existsByStudentInAndClubStatusAndClub_TypeAndClub_State(List<Student> student, ClubStatus clubStatus, ClubType clubType, ClubStatus state);

    boolean existsByStudentAndPermissionAndClub_Type(Student students, ClubPermission clubPermission, ClubType clubType);

    boolean existsByStudentInAndClub(List<Student> students, Club club);
}
