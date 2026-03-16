package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.*;
import b1nd.dodam.domain.rds.club.repository.ClubMemberRepository;
import b1nd.dodam.domain.rds.club.repository.ClubRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.enumeration.ActiveStatus;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class ClubMemberService {
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final StudentRepository studentRepository;

    public void saveClubMembers(List<ClubMember> clubMembers) {
        clubMemberRepository.saveAll(clubMembers);
    }

    public void saveAndValidateClubMembers(List<ClubMember> clubMember) {
        clubMember.forEach(c -> validateByClubAndStudent(c.getClub(), c.getStudent()));
        clubMemberRepository.saveAll(clubMember);
    }

    public void setClubMemberStatus(Long clubMemberId, Member member, ClubStatus clubStatus) {
        Student student = studentRepository.getByMember(member);
        ClubMember clubMember = clubMemberRepository.getByIdAndStudent(clubMemberId, student);
        if (clubStatus == ClubStatus.ALLOWED && clubMember.getClub().getType() == ClubType.CREATIVE_ACTIVITY_CLUB) {
            rejectActivityClubMember(student);
        }
        clubMember.modifyStatus(clubStatus);
        clubMemberRepository.save(clubMember);
    }

    public List<Student> getSecondGradeStudent(Member member) {
        return clubMemberRepository.findSecondGradeStudentsNotInClubMember(ActiveStatus.ACTIVE, member);
    }

    public List<Student> getAllGradeStudent(Member member) {
        return studentRepository.findAllByMember_StatusAndMemberNot(ActiveStatus.ACTIVE, member);
    }


    public List<ClubMember> findUserAllowedClub(Member member) {
        return clubMemberRepository.findByStudentAndClubStatusAndClub_State(studentRepository.getByMember(member), ClubStatus.ALLOWED, ClubStatus.ALLOWED);
    }

    public void setStatusStudentClub(int studentId, Long clubId, ClubStatus clubStatus) {
        ClubMember clubMember = clubMemberRepository.getPendingClubMemberWithRelations(studentId, clubId, ClubStatus.PENDING);
        if (clubStatus == ClubStatus.ALLOWED && clubMember.getStudent().getGrade() == 1) {
            long count = clubMemberRepository.countByClubIdAndClubStatusAndStudentGrade(clubId, ClubStatus.ALLOWED, 1);
            if (count >= clubMember.getClub().getMaxMemberCount()) {
                throw new OverflowMemberSizeException();
            }
        }
        clubMember.modifyStatus(clubStatus);
        clubMemberRepository.save(clubMember);
    }

    public List<ClubMember> getAllLeader() {
        return clubMemberRepository.findAllByPermission(ClubPermission.CLUB_LEADER);
    }

    public Club findClubIfNotClubMember(Long clubId, ClubStatus state, Student student, ClubStatus status) {
        return clubMemberRepository.findClubIfNotMember(clubId, state, student, status).orElseThrow(ClubJoinedException::new);
    }

    public List<ClubMember> findAllCreativeClubByStudent(Student student) {
        List<ClubMember> clubMembers = clubMemberRepository.findByStudentAndClubStatusInAndClub_TypeAndClub_State(student, List.of(ClubStatus.PENDING), ClubType.CREATIVE_ACTIVITY_CLUB, ClubStatus.ALLOWED);
        if(clubMembers.isEmpty()) {
            throw new ClubNotFoundException();
        }
        return clubMembers;
    }

    public void updateStatus(List<ClubMember> members, ClubStatus status) {
        members.forEach(member -> member.modifyStatus(status));
        List<Student> students = members.stream().map(ClubMember::getStudent).toList();
        List<ClubMember> selectedClubMembers = clubMemberRepository.findByStudentInAndClubStatusNotAndClub_Type(students, status, ClubType.CREATIVE_ACTIVITY_CLUB);
        selectedClubMembers.forEach(clubMember -> clubMember.modifyStatus(ClubStatus.REJECTED));
    }

    public List<Club> getStudentClubStatus(Student student) {
        return clubMemberRepository.findByStudentAndPermissionAndClub_StateNot(student, ClubPermission.CLUB_LEADER, ClubStatus.DELETED)
                .stream()
                .map(ClubMember::getClub)
                .collect(Collectors.toList());
    }

    public List<ClubMember> getJoinRequests(Member member, ClubStatus status) {
        Student student = studentRepository.getByMember(member);
        return clubMemberRepository.findByStudentAndClubStatus(student, status);
    }

    public List<ClubMember> getJoinRequests(Member member) {
        Student student = studentRepository.getByMember(member);
        return clubMemberRepository.findAllByStudent(student);
    }

    public ClubMember getClubLeader(Long clubId) {
        return clubMemberRepository.getByClubAndPermissionAndStatus(clubRepository.getByClubId(clubId), ClubPermission.CLUB_LEADER, ClubStatus.ALLOWED);
    }

    public void validateActiveClubMemberSize(Club club, Member leader) {
        validateByClubLeader(club, leader);
        if (clubMemberRepository.findAllByClubAndClubStatus(club, ClubStatus.ALLOWED).size() < 5) {
            throw new InsufficientClubMembersException();
        }
    }

    public List<ClubMember> getStatusClubMembers(Long clubId,  ClubStatus clubStatus) {
        return clubMemberRepository.findAllByClubAndClubStatus(clubRepository.getByClubId(clubId), clubStatus);
    }

    public List<ClubMember> getAllClubMembers(Long clubId) {
        Club club = clubRepository.getByClubId(clubId);
        return clubMemberRepository.findAllByClubAndPermission(club, ClubPermission.CLUB_MEMBER);
    }

    public void setDeleteAllClubMembers(Club club) {
        List<ClubMember> clubMembers = clubMemberRepository.findByClub(club);
        clubMembers.forEach(cm -> cm.modifyStatus(ClubStatus.DELETED));
        clubMemberRepository.saveAll(clubMembers);
    }

    public void setDeleteClubMembers(Club club) {
        List<ClubMember> clubMembers = clubMemberRepository.findByClubAndClubStatusNot(club, ClubStatus.ALLOWED);
        clubMembers.forEach(cm -> cm.modifyStatus(ClubStatus.DELETED));
        clubMemberRepository.saveAll(clubMembers);
    }

    public void validateAndRejectLeader(Club club, Student leader, List<Student> students) {
        if (club.getType() == ClubType.CREATIVE_ACTIVITY_CLUB) {
            rejectActivityClubMember(leader);
        }
        validateLeaderInList(leader, students);
        validateByLeaderAndClubMemberDuplicated(leader, students, club);
    }

    public void validateByClubLeader(Club club, Member member) {
        Student leader = studentRepository.getByMember(member);
        if(!clubMemberRepository.existsByClubAndStudentAndPermission(club, leader, ClubPermission.CLUB_LEADER)) {
            throw new ClubPermissionDeniedException();
        }
    }

    public void validateByClubAndStudent(Club clubId, Student student) {
        if (!(clubMemberRepository.findByClubAndStudentAndClubStatusNot(clubId, student, ClubStatus.DELETED) == null)) {
            throw new ClubJoinedException();
        }
    }

    public void validateFirstGrade(Student student) {
        if (student.getGrade() != 1) {
            throw new ClubApplicationNotAllowedException();
        }
    }

    public void validateNoActiveCreativeClub(Student student) {
        boolean hasActive = !clubMemberRepository.findByStudentAndClubStatusInAndClub_TypeAndClub_State(
                student, List.of(ClubStatus.ALLOWED, ClubStatus.PENDING),
                ClubType.CREATIVE_ACTIVITY_CLUB, ClubStatus.ALLOWED
        ).isEmpty();
        if (hasActive) {
            throw new ClubJoinedException();
        }
    }

    public boolean isClubLeader(Long clubId,  Member member) {
        Student leader = studentRepository.findByMember(member).orElse(null);
        if (leader == null) {
            return false;
        }
        Club club = clubRepository.getByClubId(clubId);
        ClubMember clubMember = clubMemberRepository.findByClubAndStudentAndPermission(club, leader, ClubPermission.CLUB_LEADER);
        return clubMember != null;
    }

    public long countFirstGradeAllowedMembers(Club club) {
        return clubMemberRepository.countByClubIdAndClubStatusAndStudentGrade(club.getId(), ClubStatus.ALLOWED, 1);
    }

    public void autoApproveClubsWithinLimit() {
        List<ClubMember> pendingFirstGrade = clubMemberRepository.findPendingFirstGradeByClubType(
                ClubStatus.PENDING, 1, ClubType.CREATIVE_ACTIVITY_CLUB, ClubStatus.ALLOWED
        );

        Map<Club, List<ClubMember>> byClub = pendingFirstGrade.stream()
                .collect(Collectors.groupingBy(ClubMember::getClub));

        byClub.forEach((club, members) -> {
            if (members.size() <= club.getMaxMemberCount()) {
                members.forEach(m -> m.modifyStatus(ClubStatus.ALLOWED));
                clubMemberRepository.saveAll(members);
            }
        });
    }

    private void rejectActivityClubMember(Student student) {
        List<ClubMember> clubMembers = clubMemberRepository.findAllByStudentAndPermissionAndClub_Type(student, ClubPermission.CLUB_MEMBER, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubMembers.forEach(m -> m.modifyStatus(ClubStatus.REJECTED));
        clubMemberRepository.saveAll(clubMembers);
    }

    private void validateLeaderInList(Student leader, List<Student> students) {
        if (students.stream().anyMatch(s -> s.getId() == leader.getId())) {
            throw new InvalidClubMemberInviteException();
        }
    }

    private void validateByLeaderAndClubMemberDuplicated(Student leader, List<Student> students, Club club) {
        if (club.getType() == ClubType.SELF_DIRECT_ACTIVITY_CLUB) {
            return;
        }
        if (!clubMemberRepository.findByStudentInAndClubStatusAndClub_TypeAndClub_StateNot(
                Stream.concat(Stream.of(leader), students.stream()).toList(),
                ClubStatus.ALLOWED,
                ClubType.CREATIVE_ACTIVITY_CLUB,
                ClubStatus.DELETED).isEmpty()
        ) {
            throw new InvalidClubMemberInviteException();
        }
    }
}
