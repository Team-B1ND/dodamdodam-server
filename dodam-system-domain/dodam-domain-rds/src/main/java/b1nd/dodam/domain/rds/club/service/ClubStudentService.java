package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.AlreadyClubOwnerException;
import b1nd.dodam.domain.rds.club.exception.AlreadyUserJoinCreativeClubException;
import b1nd.dodam.domain.rds.club.exception.ClubPermissionDeniedException;
import b1nd.dodam.domain.rds.club.repository.ClubStudentRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubStudentService {
    private final ClubStudentRepository clubStudentRepository;

    public void saveDirector(Club club, Student student) {
        rejectActivityClubMember(student);
        if (club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB) {
            validateClubDirectorDuplicated(student);
        }
        clubStudentRepository.save(ClubMember.builder()
                .student(student)
                .clubStatus(ClubStatus.ALLOWED)
                .club(club)
                .permission(ClubPermission.DIRECTOR)
                .build()
        );
    }

    public void saveWithBuild(Club club, List<Student> students, ClubStatus clubStatus) {
        if (clubStudentRepository.existsByStudentInAndClub(students, club)) {
            throw new AlreadyUserJoinCreativeClubException();
        }
        if (club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB) {
            validateClubMemberDuplicated(students);
        }
        clubStudentRepository.saveAll(students.stream()
                .map(student -> ClubMember.builder()
                        .student(student)
                        .clubStatus(clubStatus)
                        .club(club)
                        .permission(ClubPermission.MEMBER)
                        .build()
                ).toList()
        );
    }

    public void validateClubMemberAndDirector(Club club, Student student) {
        if(!clubStudentRepository.existsByClubAndStudentAndPermission(club, student, ClubPermission.DIRECTOR)) {
            throw new ClubPermissionDeniedException();
        }
    }

    private void validateClubDirectorDuplicated(Student student) {
        if(clubStudentRepository.existsByStudentAndPermissionAndClub_Type(student, ClubPermission.DIRECTOR, ClubType.CREATIVE_ACTIVITY_CLUB)) {
            throw new AlreadyClubOwnerException();
        }
    }

    private void validateClubMemberDuplicated(List<Student> students) {
        if (clubStudentRepository.existsByStudentInAndClubStatusAndClub_Type(students, ClubStatus.ALLOWED, ClubType.CREATIVE_ACTIVITY_CLUB)) {
            throw new AlreadyUserJoinCreativeClubException();
        }
    }

    private void rejectActivityClubMember(Student student) {
        List<ClubMember> clubMembers = clubStudentRepository.findAllByStudentAndClub_Type(student, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubMembers.forEach(m -> m.modifyStatus(ClubStatus.REJECTED));
        clubStudentRepository.saveAll(clubMembers);
    }
}
