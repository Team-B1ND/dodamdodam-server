package b1nd.dodam.restapi.support.assignment;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.restapi.club.application.data.res.ClubAcceptedMembersRes;
import b1nd.dodam.restapi.club.application.data.res.ClubAllocationResultRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.*;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class ClubAllocator {
    private static final int MAX_MEMBERS_PER_CLUB = 18;
    private final ClubMemberService clubMemberService;

    public ClubAllocationResultRes allocate(
            List<Club> clubs,
            Map<Student, List<ClubMember>> studentApplications
    ) {
        Map<Club, Integer> existingAllowedCounts = new HashMap<>();
        List<ClubAcceptedMembersRes> clubAcceptedMembersList = new ArrayList<>();

        for (Club club : clubs) {
            existingAllowedCounts.put(club, 0);
            clubAcceptedMembersList.add(new ClubAcceptedMembersRes(club.getId(), club.getName(), new ArrayList<>()));
        }

        List<ClubMember> toActivate = new ArrayList<>();
        Set<Student> assignedStudents = new HashSet<>();

        processAllPriorities(studentApplications, existingAllowedCounts, assignedStudents, toActivate, clubAcceptedMembersList);

        return new ClubAllocationResultRes(
                toActivate.size(),
                clubMemberService.getStudentsNotInClub().stream().map(StudentRes::of).toList(),
                clubAcceptedMembersList
        );
    }

    private void processAllPriorities(
            Map<Student, List<ClubMember>> studentApplications,
            Map<Club, Integer> existingAllowedCounts,
            Set<Student> assignedStudents,
            List<ClubMember> toActivate,
            List<ClubAcceptedMembersRes> clubAcceptedMembersList
    ) {
        processPriorityApplications(ClubPriority.CREATIVE_ACTIVITY_CLUB_1, studentApplications, existingAllowedCounts, assignedStudents, toActivate, clubAcceptedMembersList);
        processPriorityApplications(ClubPriority.CREATIVE_ACTIVITY_CLUB_2, studentApplications, existingAllowedCounts, assignedStudents, toActivate, clubAcceptedMembersList);
        processPriorityApplications(ClubPriority.CREATIVE_ACTIVITY_CLUB_3, studentApplications, existingAllowedCounts, assignedStudents, toActivate, clubAcceptedMembersList);
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
}
