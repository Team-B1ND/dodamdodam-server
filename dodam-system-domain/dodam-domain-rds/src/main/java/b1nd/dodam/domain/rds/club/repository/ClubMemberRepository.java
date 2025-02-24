package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.ClubMemberNotFoundException;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    default ClubMember getByIdAndStudent(long id, Student student) {
        return findByIdAndStudent(id, student).orElseThrow(ClubMemberNotFoundException::new);
    }

    default ClubMember getByClubAndPermissionAndStatus(Club club, ClubPermission permission, ClubStatus status) {
        return findByClubAndPermissionAndClubStatus(club, permission, status).orElseThrow(ClubMemberNotFoundException::new);
    }

    List<ClubMember> findAllByStudentAndPermissionAndClub_Type(Student student, ClubPermission permission, ClubType clubType);

    boolean existsByClubAndStudentAndPermission(Club club, Student student, ClubPermission permission);

    List<ClubMember> findByStudentAndClubStatus(Student student, ClubStatus clubStatus);

    Optional<ClubMember> findByClubAndPermissionAndClubStatus(Club club, ClubPermission permission, ClubStatus clubStatus);

    Optional<ClubMember> findByIdAndStudent(Long id, Student student);

    List<ClubMember> findByStudentInAndClubStatusAndClub_TypeAndClub_StateNot(List<Student> students, ClubStatus clubStatus, ClubType clubType, ClubStatus state);
}
