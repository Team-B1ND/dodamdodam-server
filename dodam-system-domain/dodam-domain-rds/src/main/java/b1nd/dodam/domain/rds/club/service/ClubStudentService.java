package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.AlreadyClubLeaderException;
import b1nd.dodam.domain.rds.club.exception.AlreadyUserJoinCreativeClubException;
import b1nd.dodam.domain.rds.club.exception.ClubPermissionDeniedException;
import b1nd.dodam.domain.rds.club.repository.ClubStudentRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubStudentService {
    private final ClubStudentRepository clubStudentRepository;

    public void saveWithBuild(Club club, Student leader, List<Student> students) {
        rejectActivityClubMember(leader);
        validateByLeaderDuplicated(leader, club);
        validateClubMemberDuplicated(students, club);
        List<ClubMember> clubMembers = new ArrayList<>(students.stream()
                .map(student -> createMember(club, student, ClubPermission.CLUB_MEMBER, ClubStatus.WAITING)).toList());
        clubMembers.add(0, createMember(club, leader,  ClubPermission.CLUB_LEADER, ClubStatus.ALLOWED));
        clubStudentRepository.saveAll(clubMembers);
    }

    public void validateByClubMemberAndLeader(Club club, Student student) {
        if(!clubStudentRepository.existsByClubAndStudentAndPermission(club, student, ClubPermission.CLUB_LEADER)) {
            throw new ClubPermissionDeniedException();
        }
    }

    private void validateByLeaderDuplicated(Student student, Club club) {
        if(club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB && clubStudentRepository.existsByStudentAndPermissionAndClub_Type(student, ClubPermission.CLUB_LEADER, ClubType.CREATIVE_ACTIVITY_CLUB)) {
            throw new AlreadyClubLeaderException();
        }
    }

    private void validateClubMemberDuplicated(List<Student> students, Club club) {
        if (clubStudentRepository.existsByStudentInAndClub(students, club)) {
            throw new AlreadyUserJoinCreativeClubException();
        }
        if (club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB && clubStudentRepository.existsByStudentInAndClubStatusAndClub_Type(students, ClubStatus.ALLOWED, ClubType.CREATIVE_ACTIVITY_CLUB)) {
            throw new AlreadyUserJoinCreativeClubException();
        }
    }

    private void rejectActivityClubMember(Student student) {
        List<ClubMember> clubMembers = clubStudentRepository.findAllByStudentAndClub_Type(student, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubMembers.forEach(m -> m.modifyStatus(ClubStatus.REJECTED));
        clubStudentRepository.saveAll(clubMembers);
    }

    private ClubMember createMember(Club club, Student student, ClubPermission clubPermission, ClubStatus clubStatus) {
        return ClubMember.builder()
                .club(club)
                .student(student)
                .permission(clubPermission)
                .clubStatus(clubStatus)
                .build();
    }
}
