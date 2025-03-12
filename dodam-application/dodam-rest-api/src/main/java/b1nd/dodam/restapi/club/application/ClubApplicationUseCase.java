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
        applyPriorityBasedClubUpdates(createClubMemberMap(clubMembers));
        clubMemberService.saveClubMembers(clubMembers);
        return Response.ok("동아리 랜덤 배정 성공");
    }

    private void applyPriorityBasedClubUpdates(Map<Club, List<ClubMember>> clubMemberMap) {
        for(ClubPriority priority : ClubPriority.getClubPriorities()) {
            clubMemberService.updateStatus(clubMemberService.shuffleClubMemberMap(MAX_STUDENT_COUNT, clubMemberMap, priority), ClubStatus.ALLOWED);
        }
    }

    private Map<Club, List<ClubMember>> createClubMemberMap(List<ClubMember> clubMembers) {
        return clubMembers.stream()
            .collect(Collectors.groupingBy(ClubMember::getClub));
    }
}
