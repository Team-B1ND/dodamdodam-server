package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubApplicationUseCase {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private static final int MAX_STUDENT_COUNT = 22;

    //TODO 기존 부원 고려하는 식으로 변경 -> 성능 개선 -> 테스트 -> 성능 개선
    public void assignmentClubMembers() {
        List<Club> clubs = clubService.getCreativeActivityClubs();
        List<ClubMember> clubMembers = clubMemberService.getPendingAndAllowedMembersByClubs(clubs);
        Map<Club, List<ClubMember>> clubMemberMap = createClubMemberMap(clubMembers);
        for(ClubPriority priority : ClubPriority.getClubPriorities()) {
            List<ClubMember> clubMemberList = clubMemberMap.entrySet().stream()
                .flatMap(entry -> {
                    List<ClubMember> members = entry.getValue();
                    int allowedCount = (int) members.stream().filter(m -> m.getClubStatus() == ClubStatus.ALLOWED).count();
                    List<ClubMember> shuffledList = members.stream().filter(member -> member.getPriority() == priority).collect(Collectors.toList());
                    Collections.shuffle(shuffledList);

                    return shuffledList.stream()
                            .filter(member -> member.getClubStatus() == ClubStatus.PENDING)
                            .limit(Math.max(0, MAX_STUDENT_COUNT - allowedCount));
                }).toList()
                .stream()
                .filter(member -> member.getPriority() == priority)
                .collect(Collectors.collectingAndThen(Collectors.toList(), list -> {
                    Collections.shuffle(list);
                    return list.subList(0, Math.min(MAX_STUDENT_COUNT-(getAllowedMemberSize(list)), list.size()));
                }))
                .stream().filter(member -> member.getClubStatus() == ClubStatus.PENDING)
                .toList();
            clubMemberService.updateStatus(clubMemberList, ClubStatus.ALLOWED);
        }
        clubMemberService.saveClubMembers(clubMembers);
    }

    private int getAllowedMemberSize(List<ClubMember> clubMembers) {
        return clubMembers.stream().filter(member -> member.getClubStatus() == ClubStatus.ALLOWED).toList().size();
    }

    private Map<Club, List<ClubMember>> createClubMemberMap(List<ClubMember> clubMembers) {
        return clubMembers.stream()
            .collect(Collectors.groupingBy(ClubMember::getClub));
    }
}
