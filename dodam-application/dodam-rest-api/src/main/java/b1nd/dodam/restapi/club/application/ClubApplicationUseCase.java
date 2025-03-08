package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.restapi.club.application.data.res.ClubAcceptedMembersRes;
import b1nd.dodam.restapi.club.application.data.res.ClubAllocationResultRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClubApplicationUseCase {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private static final int MAX_MEMBERS_PER_CLUB = 18;

    public ResponseData<ClubAllocationResultRes> sortClubMembers() {
        List<Club> clubs = clubService.getCreativeActivityClubs();
        if (clubs.isEmpty()) {
            throw new ClubNotFoundException();
        }

        Map<Club, Integer> existingAllowedCounts = new HashMap<>();
        List<ClubAcceptedMembersRes> clubAcceptedMembersList = new ArrayList<>();

        for (Club club : clubs) {
            int allowedCount = clubMemberService.getAllowedMembersByClub(club).size();
            existingAllowedCounts.put(club, allowedCount);
            clubAcceptedMembersList.add(new ClubAcceptedMembersRes(club.getName(), new ArrayList<>()));
        }

        List<ClubMember> allPendingMembers = getAllPendingMembers(clubs);
        Map<Student, List<ClubMember>> studentApplications = groupByStudent(allPendingMembers);

        List<ClubMember> toActivate = new ArrayList<>();
        List<ClubMember> toReject = new ArrayList<>();
        Set<Student> assignedStudents = new HashSet<>();

        processAllPriorities(
                studentApplications,
                existingAllowedCounts,
                assignedStudents,
                toActivate,
                clubAcceptedMembersList
        );

        List<StudentRes> rejectedStudents = allPendingMembers.stream()
                .filter(member -> !toActivate.contains(member))
                .map(member -> StudentRes.of(member.getStudent()))
                .toList();

        clubMemberService.updateStatus(toActivate, ClubStatus.ALLOWED);
        clubMemberService.updateStatus(toReject, ClubStatus.REJECTED);

        ClubAllocationResultRes result = new ClubAllocationResultRes(
                toActivate.size(),
                rejectedStudents,
                clubAcceptedMembersList
        );

        return ResponseData.ok("동아리 배정이 완료되었습니다.", result);
    }

    private void processAllPriorities(
            Map<Student, List<ClubMember>> studentApplications,
            Map<Club, Integer> existingAllowedCounts,
            Set<Student> assignedStudents,
            List<ClubMember> toActivate,
            List<ClubAcceptedMembersRes> clubAcceptedMembers
    ) {

        processPriorityApplications(
                ClubPriority.CREATIVE_ACTIVITY_CLUB_1,
                studentApplications,
                existingAllowedCounts,
                assignedStudents,
                toActivate,
                clubAcceptedMembers
        );

        processPriorityApplications(
                ClubPriority.CREATIVE_ACTIVITY_CLUB_2,
                studentApplications,
                existingAllowedCounts,
                assignedStudents,
                toActivate,
                clubAcceptedMembers
        );

        processPriorityApplications(
                ClubPriority.CREATIVE_ACTIVITY_CLUB_3,
                studentApplications,
                existingAllowedCounts,
                assignedStudents,
                toActivate,
                clubAcceptedMembers
        );
    }

    private void processPriorityApplications(
        ClubPriority priority,
        Map<Student, List<ClubMember>> studentApplications,
        Map<Club, Integer> existingAllowedCounts,
        Set<Student> assignedStudents,
        List<ClubMember> toActivate,
        List<ClubAcceptedMembersRes> clubAcceptedMembersList
    ) {

        List<ClubMember> priorityApplications = studentApplications.entrySet().stream()
                .filter(entry -> !assignedStudents.contains(entry.getKey()))
                .map(entry -> entry.getValue().stream()
                        .filter(member -> member.getPriority() == priority)
                        .findFirst().orElse(null))
                .filter(Objects::nonNull)
                .collect(Collectors.toList());

        Collections.shuffle(priorityApplications);

        for (ClubMember member : priorityApplications) {
            Club club = member.getClub();
            int currentAllowedCount = existingAllowedCounts.getOrDefault(club, 0);

            if (currentAllowedCount < MAX_MEMBERS_PER_CLUB) {
                toActivate.add(member);
                assignedStudents.add(member.getStudent());
                existingAllowedCounts.put(club, currentAllowedCount + 1);

                StudentRes studentRes = StudentRes.of(member.getStudent());

                clubAcceptedMembersList.stream()
                        .filter(c -> c.clubName().equals(club.getName()))
                        .findFirst()
                        .ifPresent(c -> c.acceptedStudents().add(studentRes));
            }
        }
    }

    private List<ClubMember> getAllPendingMembers(List<Club> clubs) {
        return clubs.stream()
                .flatMap(club -> clubMemberService.getPendingMembersByClub(club).stream())
                .toList();
    }

    private Map<Student, List<ClubMember>> groupByStudent(List<ClubMember> members) {
        return members.stream()
                .collect(Collectors.groupingBy(ClubMember::getStudent));
    }
}
