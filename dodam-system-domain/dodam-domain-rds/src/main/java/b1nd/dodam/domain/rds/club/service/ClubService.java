package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubDuplicateException;
import b1nd.dodam.domain.rds.club.repository.ClubRepository;
import b1nd.dodam.domain.rds.club.repository.ClubMemberRepository;
import b1nd.dodam.domain.rds.member.entity.Student;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ClubService {
    private final ClubRepository clubRepository;
    private final ClubMemberRepository clubMemberRepository;
    private final String DELETED_PREFIX =  "_deleted";

    public void checkIsNameDuplicated(String name) {
        if (clubRepository.existsByName(name)) {
            throw new ClubDuplicateException();
        }
    }

    public void update(Club club) {
        clubRepository.save(club);
    }

    public void updateAll(List<Club> clubs) {
        clubRepository.saveAll(clubs);
    }

    public void saveClubAndMember(Club club, Student leader, List<Student> students) {
        clubRepository.save(club);
        Set<ClubMember> clubMembers = students.stream()
                .map(student -> createMember(club, student, ClubPermission.CLUB_MEMBER, ClubStatus.WAITING)).collect(Collectors.toSet());
        clubMembers.add(createMember(club, leader, ClubPermission.CLUB_LEADER, ClubStatus.ALLOWED));
        clubMemberRepository.saveAll(clubMembers);
    }

    public List<Club> findAll() {
        return clubRepository.findAll();
    }

    public Club findById(Long id) {
        return clubRepository.getByClubId(id);
    }

    public List<Club> findByIds(List<Long> ids) {
        return clubRepository.findByIdIn(ids);
    }

    public void deleteClub(Club club) {
        club.updateStatus(club.getName() + DELETED_PREFIX, ClubStatus.DELETED);
        clubRepository.save(club);
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
