package b1nd.dodam.domain.rds.club.repository;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.ClubMemberNotFoundException;
import b1nd.dodam.domain.rds.member.entity.Student;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ClubMemberRepository extends JpaRepository<ClubMember, Long> {
    default ClubMember getByIdAndStudent(long id, Student student) {
        return findByIdAndStudent(id, student).orElseThrow(ClubMemberNotFoundException::new);
    }

    default ClubMember getByClubMemberId(long id) {
        return findById(id).orElseThrow(ClubMemberNotFoundException::new);
    }

    default ClubMember getByClubAndPermissionAndStatus(Club club, ClubPermission permission, ClubStatus status) {
        return findByClubAndPermissionAndClubStatus(club, permission, status).orElseThrow(ClubMemberNotFoundException::new);
    }

    default ClubMember getPendingClubMemberWithRelations(int studentId, Long clubId, ClubStatus status) {
        return findPendingClubMemberWithRelations(studentId, clubId, status).orElseThrow(ClubMemberNotFoundException::new);
    }

    List<ClubMember> findAllByStudentAndPermissionAndClub_Type(Student student, ClubPermission permission, ClubType clubType);

    boolean existsByClubAndStudentAndPermission(Club club, Student student, ClubPermission permission);

    @EntityGraph(attributePaths = {"club", "club.teacher"})
    List<ClubMember> findByStudentAndClubStatus(Student student, ClubStatus clubStatus);

    @EntityGraph(attributePaths = {"club", "club.teacher"})
    List<ClubMember> findByStudentAndClubStatusAndClub_State(Student student, ClubStatus clubStatus, ClubStatus state);

    List<ClubMember> findByStudentAndPermission(Student student, ClubPermission permission);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<ClubMember> findAllByClubAndPermission(Club club, ClubPermission permission);

    @EntityGraph(attributePaths = {"student", "student.member"})
    List<ClubMember> findAllByClubAndClubStatus(Club club, ClubStatus clubStatus);

    @Query("""
    SELECT c FROM club c
    LEFT JOIN club_member cm ON c.id = cm.club.id
        AND cm.student = :student
        AND cm.clubStatus != :status
    WHERE c.id = :clubId
    AND c.state = :state
    AND cm.id IS NULL
    """)
    Club findClubIfNotMember(
            @Param("clubId") Long clubId,
            @Param("state") ClubStatus state,
            @Param("student") Student student,
            @Param("status") ClubStatus status
    );

    @Query("""
    SELECT s, m FROM student s
    LEFT JOIN FETCH member m
    LEFT JOIN club_member cm ON s.id = cm.student.id AND cm.clubStatus = 'ALLOWED'
    WHERE cm.student.id IS NULL
    AND s.grade = 2
    """)
    List<Student> findSecondGradeStudentsNotInClubMember();

    @Query("""
    SELECT cm FROM club_member cm
    JOIN FETCH cm.student s
    JOIN FETCH cm.club c
    WHERE s.id = :studentId AND c.id = :clubId AND cm.clubStatus = :status
    """)
    Optional<ClubMember> findPendingClubMemberWithRelations(
            @Param("studentId") int studentId,
            @Param("clubId") Long clubId,
            @Param("status") ClubStatus status
    );

    @Modifying
    @Query("""
    UPDATE club_member cm
    SET cm.clubStatus = :status
    WHERE cm.student.id = :studentId
    AND cm.permission = :permission
    AND cm.club.id IN (
        SELECT c.id FROM club c WHERE c.type = :clubType
    )
    """)
    void rejectOtherActivityClubMembers(
            @Param("studentId") int studentId,
            @Param("status") ClubStatus status,
            @Param("permission") ClubPermission permission,
            @Param("clubType") ClubType clubType
    );

    Optional<ClubMember> findByClubAndPermissionAndClubStatus(Club club, ClubPermission permission, ClubStatus clubStatus);

    Optional<ClubMember> findByIdAndStudent(Long id, Student student);

    ClubMember findByIdAndStudentAndPermission(Long id, Student student, ClubPermission permission);

    List<ClubMember> findByStudentAndClubStatusAndClub_TypeAndClub_State(Student student, ClubStatus clubStatus, ClubType clubType, ClubStatus clubState);

    List<ClubMember> findByStudentInAndClubStatusAndClub_TypeAndClub_StateNot(List<Student> students, ClubStatus clubStatus, ClubType clubType, ClubStatus state);

    ClubMember findByClubAndStudentAndClubStatus(Club club, Student student, ClubStatus clubStatus);

    List<ClubMember> findByClubAndStudent(Club club, Student student);
}
