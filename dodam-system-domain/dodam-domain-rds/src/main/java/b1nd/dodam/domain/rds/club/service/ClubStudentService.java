package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStudentStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.AlreadyClubOwnerException;
import b1nd.dodam.domain.rds.club.exception.AlreadyInTheClubException;
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

    public void validateClubMemberAndDirector(Club club, Student student) {
        if(!clubStudentRepository.existsByClubAndStudentAndPermission(club, student, ClubPermission.DIRECTOR)) {
            throw new ClubPermissionDeniedException();
        }
    }

    public void saveDirector(Club club, Student student) {
        rejectActivityClubMember(student);
        if (club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB) {
            validateClubDirectorDuplicated(student);
        }
        clubStudentRepository.save(ClubMember.builder()
                .student(student)
                .clubStudentStatus(ClubStudentStatus.ALLOWED)
                .club(club)
                .permission(ClubPermission.DIRECTOR)
                .build()
        );
    }

    public void saveWithBuild(Club club, List<Student> students, ClubStudentStatus clubStudentStatus) {
        if (clubStudentRepository.existsByStudentInAndClub(students, club)) {
            throw new AlreadyInTheClubException();
        }
        if (club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB) {
            validateClubMemberDuplicated(students);
        }
        clubStudentRepository.saveAll(students.stream()
                .map(student -> ClubMember.builder()
                        .student(student)
                        .clubStudentStatus(clubStudentStatus)
                        .club(club)
                        .permission(ClubPermission.MEMBER)
                        .build()
                ).toList()
        );
    }

    private void validateClubDirectorDuplicated(Student student) {
        if(clubStudentRepository.existsByStudentAndPermissionAndClub_Type(student, ClubPermission.DIRECTOR, ClubType.CREATIVE_ACTIVITY_CLUB)) {
            throw new AlreadyClubOwnerException();
        }
    }

    private void validateClubMemberDuplicated(List<Student> students) {
        if (clubStudentRepository.existsByStudentInAndClubStatusAndClub_Type(students, ClubStudentStatus.ALLOWED, ClubType.CREATIVE_ACTIVITY_CLUB)) {
            throw new AlreadyUserJoinCreativeClubException();
        }
    }

    private void rejectActivityClubMember(Student student) {
        List<ClubMember> clubMembers = clubStudentRepository.findAllByStudentAndClub_Type(student, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubMembers.forEach(m -> m.modifyStatus(ClubStudentStatus.REJECTED));
        clubStudentRepository.saveAll(clubMembers);
    }
}
