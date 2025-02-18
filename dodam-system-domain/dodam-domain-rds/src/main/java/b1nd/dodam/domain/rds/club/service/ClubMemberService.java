package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubMemberStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.exception.AlreadyInTheClubException;
import b1nd.dodam.domain.rds.club.repository.ClubMemberRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import lombok.RequiredArgsConstructor;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;

    public void rejectActivityClubMember(Member member) {
        List<ClubMember> clubMembers = clubMemberRepository.findAllByMemberAndClub_Type(member, ClubType.CREATIVE_ACTIVITY_CLUB);
        clubMembers.forEach(m -> m.modifyStatus(ClubMemberStatus.REJECTED));
        clubMemberRepository.saveAll(clubMembers);
    }

    public void saveOwner(Club club, Member member) {
        clubMemberRepository.save(ClubMember.builder()
                .member(member)
                .clubStatus(ClubMemberStatus.ALLOWED)
                .club(club)
                .permission(ClubPermission.OWNER)
                .build()
        );
    }

    public void saveWithBuild(Club club, List<Member> members, ClubMemberStatus clubMemberStatus) {
        try {
            clubMemberRepository.saveAll(members.stream()
                    .map(member -> ClubMember.builder()
                            .member(member)
                            .clubStatus(clubMemberStatus)
                            .club(club)
                            .permission(ClubPermission.MEMBER)
                            .build()
                    ).toList()
            );
        } catch (DataIntegrityViolationException e) {
            throw new AlreadyInTheClubException();
        }
    }
}
