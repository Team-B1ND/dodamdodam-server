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
        Map<String, List<String>> clubAcceptedMembers = new HashMap<>();

        for (Club club : clubs) {
            int allowedCount = clubMemberService.getAllowedMembersByClub(club).size();
            existingAllowedCounts.put(club, allowedCount);
            clubAcceptedMembers.put(club.getName(), new ArrayList<>());
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
                clubAcceptedMembers
        );

        List<String> rejectedNames = new ArrayList<>();
        for (ClubMember member : allPendingMembers) {
            if (!toActivate.contains(member)) {
                toReject.add(member);
                rejectedNames.add(member.getStudent().getMember().getName());
            }
        }

        clubMemberService.updateStatus(toActivate, ClubStatus.ALLOWED);
        clubMemberService.updateStatus(toReject, ClubStatus.REJECTED);

        Map<String, Object> responseData = new HashMap<>();
        responseData.put("배정된 학생 수", toActivate.size());
        responseData.put("배정되지 못한 학생 목록", rejectedNames);
        responseData.put("동아리별 합격 멤버", clubAcceptedMembers);

        return ResponseData.ok("동아리 배정이 완료되었습니다.", responseData);
    }

    private void processAllPriorities(
            Map<Student, List<ClubMember>> studentApplications,
            Map<Club, Integer> existingAllowedCounts,
            Set<Student> assignedStudents,
            List<ClubMember> toActivate,
            Map<String, List<String>> clubAcceptedMembers) {

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
            Map<String, List<String>> clubAcceptedMembers) {

        List<ClubMember> priorityApplications = new ArrayList<>();

        for (Map.Entry<Student, List<ClubMember>> entry : studentApplications.entrySet()) {
            if (!assignedStudents.contains(entry.getKey())) {
                for (ClubMember member : entry.getValue()) {
                    if (member.getPriority() == priority) {
                        priorityApplications.add(member);
                        break;
                    }
                }
            }
        }

        Collections.shuffle(priorityApplications);

        for (ClubMember member : priorityApplications) {
            Club club = member.getClub();
            int currentAllowedCount = existingAllowedCounts.getOrDefault(club, 0);

            if (currentAllowedCount < MAX_MEMBERS_PER_CLUB) {
                toActivate.add(member);
                assignedStudents.add(member.getStudent());

                existingAllowedCounts.put(club, currentAllowedCount + 1);

                String clubName = club.getName();
                String studentName = member.getStudent().getMember().getName();
                clubAcceptedMembers.get(clubName).add(studentName);
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
