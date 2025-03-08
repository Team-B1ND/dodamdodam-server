package b1nd.dodam;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.enumeration.ClubPermission;
import b1nd.dodam.domain.rds.club.enumeration.ClubPriority;
import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.enumeration.ClubType;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.restapi.club.application.ClubApplicationUseCase;
import b1nd.dodam.restapi.club.application.data.res.ClubAcceptedMembersRes;
import b1nd.dodam.restapi.club.application.data.res.ClubAllocationResultRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;
import b1nd.dodam.restapi.support.data.ResponseData;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.*;
import java.util.stream.Collectors;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ClubJoinTest {

    @InjectMocks
    private ClubApplicationUseCase clubApplicationUseCase;

    @Mock
    private ClubService clubService;

    @Mock
    private ClubMemberService clubMemberService;

    private List<Student> students;
    private List<Club> clubs;
    private List<ClubMember> pendingMembers;

    private static final int MAX_MEMBERS_PER_CLUB = 24;

    @BeforeEach
    void setUp() {
        // 12명의 학생을 생성 (빌더 패턴 적용)
        students = new ArrayList<>();
        for (int i = 1; i <= 100; i++) {
            students.add(
                    Student.builder()
                            .id(i)
                            .member(Member.builder().name("학생" + i).build())
                            .grade(i % 3 + 1)
                            .room(i % 4 + 1)
                            .number(i)
                            .code("S" + i)
                            .busSubscribe(false)
                            .build()
            );
        }

        // 8개의 창체 동아리 생성
        clubs = new ArrayList<>();
        for (int i = 101; i <= 108; i++) {
            clubs.add(new Club("창체동아리" + (i - 100), "ㅇㅇ", "ㅇㅇ", "이미", "d", ClubType.CREATIVE_ACTIVITY_CLUB, ClubStatus.ALLOWED));
        }

        // 학생들이 3개씩 동아리에 지원하도록 설정 (더 골고루 분포)
        pendingMembers = new ArrayList<>();

        // 각 동아리별 지원 학생 수를 추적하기 위한 맵
        Map<Club, Integer> clubApplicationCounts = new HashMap<>();
        for (Club club : clubs) {
            clubApplicationCounts.put(club, 0);
        }

        for (Student student : students) {
            List<Club> availableClubs = new ArrayList<>(clubs);
            Set<Club> selectedClubs = new HashSet<>();

            // 학생의 학년에 따라 선호도를 주기 위해 셔플 전에 정렬
            availableClubs.sort((c1, c2) -> {
                // 1학년은 앞쪽 동아리, 2학년은 중간 동아리, 3학년은 뒤쪽 동아리를 선호하도록
                int grade = student.getGrade();
                int c1Index = clubs.indexOf(c1);
                int c2Index = clubs.indexOf(c2);

                if (grade == 1) {
                    return c1Index - c2Index; // 앞쪽 동아리 선호
                } else if (grade == 3) {
                    return c2Index - c1Index; // 뒤쪽 동아리 선호
                }
                return 0; // 2학년은 무작위
            });

            // 이후 조금의 랜덤성 추가
            Collections.shuffle(availableClubs.subList(0, availableClubs.size() / 2));

            int priority = 1;
            // 지원 수가 적은 동아리부터 우선 배정
            while (selectedClubs.size() < 3 && !availableClubs.isEmpty()) {
                // 지원자가 가장 적은 동아리 선택
                availableClubs.sort(Comparator.comparing(clubApplicationCounts::get));
                Club club = availableClubs.remove(0);

                selectedClubs.add(club);
                clubApplicationCounts.put(club, clubApplicationCounts.get(club) + 1);

                pendingMembers.add(new ClubMember(student, club, ClubPriority.valueOf("CREATIVE_ACTIVITY_CLUB_" + priority), ClubStatus.PENDING, ClubPermission.CLUB_MEMBER, ""));
                priority++;
            }
        }

        // 디버깅 정보 출력
        System.out.println("=== 동아리별 지원 현황 ===");
        for (Club club : clubs) {
            long count = pendingMembers.stream()
                    .filter(member -> member.getClub().equals(club))
                    .count();
            System.out.println(club.getName() + ": " + count + "명 지원");
        }
    }

    @Test
    void 창체_동아리_정렬_및_배정_테스트() {
        // Given
        when(clubService.getCreativeActivityClubs()).thenReturn(clubs);
        when(clubMemberService.getPendingMembersByClub(any())).thenAnswer(invocation -> {
            Club club = invocation.getArgument(0);
            return pendingMembers.stream()
                    .filter(member -> member.getClub().equals(club))
                    .collect(Collectors.toList());
        });
        when(clubMemberService.getAllowedMembersByClub(any())).thenReturn(Collections.emptyList());

        // When
        ResponseData<?> response = clubApplicationUseCase.sortClubMembers();

        // 결과를 JSON으로 출력
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            String json = objectMapper.writerWithDefaultPrettyPrinter()
                    .writeValueAsString(response.getData());
            System.out.println("ClubApplicationUseCase 반환 결과 (JSON):");
            System.out.println(json);
        } catch (Exception e) {
            System.err.println("JSON 변환 중 오류 발생: " + e.getMessage());
        }

        // Then
        verify(clubMemberService, times(1)).updateStatus(anyList(), eq(ClubStatus.ALLOWED));
        verify(clubMemberService, times(1)).updateStatus(anyList(), eq(ClubStatus.REJECTED));

        ClubAllocationResultRes result = (ClubAllocationResultRes) response.getData();

        // 배정된 학생 수 확인
        int assignedCount = result.assignedStudentCount();
        assertThat(assignedCount).isLessThanOrEqualTo(students.size());

        // 배정되지 못한 지원 수 검증
        List<StudentRes> rejectedStudents = result.unassignedStudents();
        int expectedRejections = pendingMembers.size() - assignedCount;
        assertThat(rejectedStudents.size()).isEqualTo(expectedRejections);

        // 동아리별 합격 멤버 목록 검증
        List<ClubAcceptedMembersRes> clubAcceptedMembers = result.clubAcceptedMembers();
        for (ClubAcceptedMembersRes entry : clubAcceptedMembers) {
            assertThat(entry.acceptedStudents().size()).isLessThanOrEqualTo(MAX_MEMBERS_PER_CLUB);
        }
    }
}
