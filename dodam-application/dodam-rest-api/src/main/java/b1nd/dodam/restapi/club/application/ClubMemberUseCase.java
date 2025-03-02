package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.member.entity.Student;
import b1nd.dodam.domain.rds.member.repository.StudentRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.JoinClubMemberReq;
import b1nd.dodam.restapi.club.application.data.res.ClubDetailRes;
import b1nd.dodam.restapi.club.application.data.res.ClubMemberRes;
import b1nd.dodam.restapi.club.application.data.res.ClubStatusRes;
import b1nd.dodam.restapi.club.application.data.res.ClubStudentRes;
import b1nd.dodam.restapi.member.application.data.res.StudentWithImageRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubMemberUseCase {
    private final ClubMemberService clubMemberService;
    private final MemberAuthenticationHolder authenticationHolder;
    private final StudentRepository studentRepository;

    public Response joinClubs(List<JoinClubMemberReq> reqs) {
        Student student = studentRepository.getByMember(authenticationHolder.current());
        boolean clubJoined = clubMemberService.isCreativeClubJoined(student);
        if (clubJoined) {
            filterCreativeClub(reqs);
        }
        clubMemberService.saveClubMembers(reqs.parallelStream()
            .map(req -> req.toEntity(student, clubMemberService.findClubIfNotClubMember(req.clubId(), ClubStatus.ALLOWED, student, ClubStatus.DELETED)))
            .toList()
        );
        return Response.ok("동아리 입부 신청 성공");
    }

    public Response updateClubJoinRequestReceived(Long id, ClubStatus clubStatus) {
        clubMemberService.setClubMemberStatus(id,authenticationHolder.current(), clubStatus);
        return Response.ok("동아리 가입 업데이트 성공");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<StudentWithImageRes>> getSecondGradeStudents() {
        return ResponseData.ok("2학년 불러오기 성공", clubMemberService.getSecondGradeStudent().stream().map(StudentWithImageRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubMemberRes>> getClubJoinRequestsReceived() {
        return ResponseData.ok("받은 부원 제안 불러오기 성공", clubMemberService.getJoinRequests(authenticationHolder.current()).stream().map(ClubMemberRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<ClubStudentRes> getClubLeader(Long id) {
        return ResponseData.ok("부장 불러오기 성공", ClubStudentRes.of(clubMemberService.getClubLeader(id)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubStudentRes>> getAllClubMembers(Long id) {
        return ResponseData.ok("동아리 모든 멤버 불러오기 성공", clubMemberService.getAllClubMembers(authenticationHolder.current(), id).stream().map(ClubStudentRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubStudentRes>> getActiveClubMembers(Long id) {
        return ResponseData.ok("동아리 멤버 불러오기 성공", clubMemberService.getActiveClubMembers(id).stream().map(ClubStudentRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubStatusRes>> getJoinedClubs() {
        return ResponseData.ok("나의 동아리 상태 불러오기 성공", clubMemberService.findUserAllowedClub(authenticationHolder.current()).stream().map(ClubStatusRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubDetailRes>> getStudentClubStatus() {
        return ResponseData.ok("동아리 불러오기 성공", clubMemberService.getStudentClubStatus(studentRepository.getByMember(authenticationHolder.current())).stream().map(ClubDetailRes::of).toList());
    }

    private void filterCreativeClub(List<JoinClubMemberReq> reqs) {
        reqs.removeIf(req -> req.clubPriority() != null);
    }

}
