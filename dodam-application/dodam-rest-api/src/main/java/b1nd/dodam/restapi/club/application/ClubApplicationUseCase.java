package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubApplicationUseCase {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private static final int MAX_STUDENT_COUNT = 22;

    public Response assignmentClubMembers() {
        List<Club> clubs = clubService.getCreativeActivityClubs();
        List<ClubMember> clubMembers = clubMemberService.getPendingAndAllowedMembersByClubs(clubs);
        Map<Club, List<ClubMember>> clubMemberMap = createClubMemberMap(clubMembers);
        for(ClubPriority priority : ClubPriority.getClubPriorities()) {
            List<ClubMember> clubMemberList = clubMemberMap.entrySet().stream()
                    .flatMap(entry -> {
                        Club club = entry.getKey();
                        List<ClubMember> members = entry.getValue();
                        int remainingSlots = MAX_STUDENT_COUNT - getAllowedMemberSize(club, members);
                        if (remainingSlots <= 0) return Stream.empty();
                        List<ClubMember> priorityMembers = members.stream()
                                .filter(member -> member.getPriority() == priority && member.getClubStatus() == ClubStatus.PENDING)
                                .collect(Collectors.toList());
                        Collections.shuffle(priorityMembers);
                        List<ClubMember> selectedMembers = priorityMembers.subList(0, Math.min(remainingSlots, priorityMembers.size()));
                        return selectedMembers.stream();
                    })
                    .toList();
            clubMemberService.updateStatus(clubMemberList, ClubStatus.ALLOWED);
        }
        clubMemberService.saveClubMembers(clubMembers);
        return Response.ok("동아리 랜덤 배정 성공");
    }

    private int getAllowedMemberSize(Club club, List<ClubMember> clubMembers) {
        return clubMembers.stream().filter(
            member ->
                member.getClubStatus() == ClubStatus.ALLOWED
                && member.getClub().getId().equals(club.getId())
        ).toList().size();
    }

    private Map<Club, List<ClubMember>> createClubMemberMap(List<ClubMember> clubMembers) {
        return clubMembers.stream()
            .collect(Collectors.groupingBy(ClubMember::getClub));
    }
}
