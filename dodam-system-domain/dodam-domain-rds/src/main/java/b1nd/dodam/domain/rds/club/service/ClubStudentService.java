package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.AlreadyUserJoinCreativeClubException;
import b1nd.dodam.domain.rds.club.exception.ClubPermissionDeniedException;
import b1nd.dodam.domain.rds.club.repository.ClubStudentRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubStudentService {
    private final ClubStudentRepository clubStudentRepository;
    private final StudentRepository studentRepository;

    public void validateAndRejectLeader(Club club, Student leader, List<Student> students) {
        rejectActivityClubMember(leader);
        validateLeaderInList(leader, students);
        validateByLeaderAndClubMemberDuplicated(leader, students, club);
    }

    public void saveWithBuild(Club club, Student leader, List<Student> students) {
        Set<ClubMember> clubMembers = students.stream()
            .map(student -> createMember(club, student, ClubPermission.CLUB_MEMBER, ClubStatus.WAITING)).collect(Collectors.toSet());
        clubMembers.add(createMember(club, leader, ClubPermission.CLUB_LEADER, ClubStatus.ALLOWED));
        clubStudentRepository.saveAll(clubMembers);
    }

    public void validateByClubLeader(Club club, Member member) {
        Student leader = studentRepository.getByMember(member);
        if(!clubStudentRepository.existsByClubAndStudentAndPermission(club, leader, ClubPermission.CLUB_LEADER)) {
            throw new ClubPermissionDeniedException();
        }
    }

    private void rejectActivityClubMember(Student student) {
        List<ClubMember> clubMembers = clubStudentRepository.findAllByStudentAndPermissionAndClub_Type(student, ClubPermission.CLUB_MEMBER, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubMembers.forEach(m -> m.modifyStatus(ClubStatus.REJECTED));
        clubStudentRepository.saveAll(clubMembers);
    }

    private void validateLeaderInList(Student leader, List<Student> students) {
        if (students.stream().anyMatch(s -> s.getId() == leader.getId())) {
            throw new AlreadyUserJoinCreativeClubException();
        }
    }

    private void validateByLeaderAndClubMemberDuplicated(Student leader, List<Student> students, Club club) {
        if (club.getType() == ClubType.SELF_DIRECT_ACTIVITY_CLUB) {
            return;
        }
        if (!clubStudentRepository.findByStudentInAndClubStatusAndClub_TypeAndClub_StateNot(
                Stream.concat(Stream.of(leader), students.stream()).toList(),
                ClubStatus.ALLOWED,
                ClubType.CREATIVE_ACTIVITY_CLUB,
                ClubStatus.DELETED).isEmpty()
        ) {
            throw new AlreadyUserJoinCreativeClubException();
        }
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
