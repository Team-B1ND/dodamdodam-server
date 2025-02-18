package b1nd.dodam.domain.rds.club.service;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.repository.ClubMemberRepository;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.support.enumeration.ApprovalStatus;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class ClubMemberService {
    private final ClubMemberRepository clubMemberRepository;

    public void deleteActivityClubMember(Member member) {
        clubMemberRepository.deleteAllByMember(member);
    }

    public void saveOwner(Club club, Member member) {
        clubMemberRepository.save(ClubMember.builder()
                .member(member)
                .clubStatus(ClubStatus.ALLOWED)
                .club(club)
                .permission(ClubPermission.OWNER)
                .build()
        );
    }

    public void saveWithBuild(Club club, List<Member> members, ClubStatus clubStatus) {
        clubMemberRepository.saveAll(members.stream()
            .map(member -> ClubMember.builder()
                .club(club)
                .member(member)
                .clubStatus(clubStatus)
                .build()
            ).toList()
        );
    }
}
