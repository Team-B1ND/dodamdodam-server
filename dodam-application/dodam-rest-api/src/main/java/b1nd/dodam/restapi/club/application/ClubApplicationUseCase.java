package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.exception.ClubNotFoundException;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.entity.Student;
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

    public ResponseData<?> sortClubMembers() {
        List<Club> clubs = clubService.getCreativeActivityClubs();
        if (clubs.isEmpty()) {
            throw new ClubNotFoundException();
        }

        int totalSlots = 72;
        int clubQuota = (totalSlots / clubs.size()) + 4;

        List<ClubMember> allPendingMembers = getAllPendingMembers(clubs);
        Map<Student, List<ClubMember>> studentApplications = groupByStudent(allPendingMembers);
        Map<Club, Integer> clubAssignmentCounts = initializeClubCounts(clubs);
        Map<String, List<String>> clubAcceptedMembers = initializeClubAcceptedMembers(clubs);
        List<ClubMember> toActivate = new ArrayList<>();
        List<ClubMember> toReject = new ArrayList<>();
        List<String> rejectedNames = new ArrayList<>();
        Set<Student> assignedStudents = new HashSet<>();
        int totalAssigned = 0;

        // 1. 1지망부터 처리
        processApplicationsByPriority(
                ClubPriority.CREATIVE_ACTIVITY_CLUB_1,
                studentApplications,
                clubAssignmentCounts,
                clubQuota,
                assignedStudents,
                toActivate,
                clubAcceptedMembers,
                totalSlots,
                totalAssigned
        );

        // 현재까지 배정된 학생 수 업데이트
        totalAssigned = toActivate.size();

        // 2. 2지망 처리
        if (totalAssigned < totalSlots) {
            processApplicationsByPriority(
                    ClubPriority.CREATIVE_ACTIVITY_CLUB_2,
                    studentApplications,
                    clubAssignmentCounts,
                    clubQuota,
                    assignedStudents,
                    toActivate,
                    clubAcceptedMembers,
                    totalSlots,
                    totalAssigned
            );
        }

        // 현재까지 배정된 학생 수 업데이트
        totalAssigned = toActivate.size();

        // 3. 3지망 처리
        if (totalAssigned < totalSlots) {
            processApplicationsByPriority(
                    ClubPriority.CREATIVE_ACTIVITY_CLUB_3,
                    studentApplications,
                    clubAssignmentCounts,
                    clubQuota,
                    assignedStudents,
                    toActivate,
                    clubAcceptedMembers,
                    totalSlots,
                    totalAssigned
            );
        }

        // 배정되지 않은 모든 지원자 거부 처리
        for (ClubMember member : allPendingMembers) {
            if (!toActivate.contains(member)) {
                toReject.add(member);
                rejectedNames.add(member.getStudent().getMember().getName());
            }
        }

        // 상태 업데이트 및 결과 반환
        clubMemberService.updateStatus(toActivate, ClubStatus.ALLOWED);
        clubMemberService.updateStatus(toReject, ClubStatus.REJECTED);

        // 응답 데이터에 동아리별 합격 멤버 리스트 추가
        Map<String, Object> responseData = new HashMap<>();
        responseData.put("배정된 학생 수", toActivate.size());
        responseData.put("배정되지 못한 학생 목록", rejectedNames);
        responseData.put("동아리별 합격 멤버", clubAcceptedMembers);

        return ResponseData.ok("동아리 배정이 완료되었습니다.", responseData);
    }

    // 우선순위별 지원자 처리
    private void processApplicationsByPriority(
            ClubPriority priority,
            Map<Student, List<ClubMember>> studentApplications,
            Map<Club, Integer> clubAssignmentCounts,
            int clubQuota,
            Set<Student> assignedStudents,
            List<ClubMember> toActivate,
            Map<String, List<String>> clubAcceptedMembers,
            int totalSlots,
            int currentAssigned) {

        // 해당 우선순위의 지원자 필터링 (이미 배정된 학생 제외)
        List<ClubMember> priorityApplications = new ArrayList<>();

        for (Map.Entry<Student, List<ClubMember>> entry : studentApplications.entrySet()) {
            if (!assignedStudents.contains(entry.getKey())) {
                for (ClubMember member : entry.getValue()) {
                    if (member.getPriority() == priority) {
                        priorityApplications.add(member);
                        break; // 한 학생의 같은 우선순위는 한 번만 추가
                    }
                }
            }
        }

        Collections.shuffle(priorityApplications);

        for (ClubMember member : priorityApplications) {
            if (currentAssigned >= totalSlots) {
                break;
            }

            Club club = member.getClub();
            int currentClubCount = clubAssignmentCounts.getOrDefault(club, 0);

            if (currentClubCount < clubQuota) {
                toActivate.add(member);
                assignedStudents.add(member.getStudent());
                clubAssignmentCounts.put(club, currentClubCount + 1);
                currentAssigned++;

                String clubName = club.getName();
                String studentName = member.getStudent().getMember().getName();
                clubAcceptedMembers.get(clubName).add(studentName);
            }
        }
    }

    private List<ClubMember> getAllPendingMembers(List<Club> clubs) {
        return clubs.stream()
                .flatMap(club -> clubMemberService.getPendingMembersByClub(club).stream())
                .collect(Collectors.toList());
    }

    private Map<Student, List<ClubMember>> groupByStudent(List<ClubMember> members) {
        return members.stream()
                .collect(Collectors.groupingBy(ClubMember::getStudent));
    }

    private Map<Club, Integer> initializeClubCounts(List<Club> clubs) {
        return clubs.stream()
                .collect(Collectors.toMap(club -> club, club -> 0));
    }

    private Map<String, List<String>> initializeClubAcceptedMembers(List<Club> clubs) {
        return clubs.stream()
                .collect(Collectors.toMap(
                        Club::getName,
                        club -> new ArrayList<>()  // 빈 리스트로 초기화
                ));
    }
}
