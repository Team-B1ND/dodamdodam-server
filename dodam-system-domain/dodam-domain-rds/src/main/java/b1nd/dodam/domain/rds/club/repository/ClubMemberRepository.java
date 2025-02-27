package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.ClubMemberNotFoundException;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    default ClubMember getByIdAndStudent(long id, Student student) {
        return findByIdAndStudent(id, student).orElseThrow(ClubMemberNotFoundException::new);
    }

    default ClubMember getByClub_IdAndPermissionAndStatus(Long club, ClubPermission permission, ClubStatus status) {
        return findByClub_IdAndPermissionAndClubStatus(club, permission, status).orElseThrow(ClubMemberNotFoundException::new);
    }

    List<ClubMember> findAllByStudentAndPermissionAndClub_Type(Student student, ClubPermission permission, ClubType clubType);

    boolean existsByClubAndStudentAndPermission(Club club, Student student, ClubPermission permission);

    List<ClubMember> findByStudentAndClubStatus(Student student, ClubStatus clubStatus);

    List<ClubMember> findAllByClubAndPermission(Club club, ClubPermission permission);

    List<ClubMember> findAllByClub_IdAndClubStatus(Long clubId, ClubStatus clubStatus);

    @Query("""
    SELECT s, m FROM student s
    LEFT JOIN FETCH member m
    LEFT JOIN club_member cm ON s.id = cm.student.id AND cm.clubStatus = 'ALLOWED'
    WHERE cm.student.id IS NULL
    AND s.grade = 2
    """)
    List<Student> findSecondGradeStudentsNotInClubMember();

    Optional<ClubMember> findByClub_IdAndPermissionAndClubStatus(Long club, ClubPermission permission, ClubStatus clubStatus);

    Optional<ClubMember> findByIdAndStudent(Long id, Student student);

    List<ClubMember> findByStudentInAndClubStatusAndClub_TypeAndClub_StateNot(List<Student> students, ClubStatus clubStatus, ClubType clubType, ClubStatus state);
}
