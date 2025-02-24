package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.res.ClubMemberRes;
import b1nd.dodam.restapi.club.application.data.res.ClubStudentRes;
import b1nd.dodam.restapi.member.application.data.res.StudentRes;
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

    public Response acceptClubJoinRequestReceived(Long id) {
        clubMemberService.setClubMemberStatus(id,authenticationHolder.current(), ClubStatus.ALLOWED);
        return Response.ok("동아리 가입 수락됨");
    }

    public Response rejectClubJoinRequestReceived(Long id) {
        clubMemberService.setClubMemberStatus(id, authenticationHolder.current(), ClubStatus.REJECTED);
        return Response.ok("동아리 가입 거절됨");
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubMemberRes>> getClubJoinRequestsReceived() {
        return ResponseData.ok("받은 부원 제안", clubMemberService.getJoinRequests(authenticationHolder.current()).stream().map(ClubMemberRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<StudentRes> getClubLeader(Long id) {
        return ResponseData.ok("부장 로드됨", StudentRes.of(clubMemberService.getClubLeader(id)));
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubStudentRes>> getAllClubMembers(Long id) {
        return ResponseData.ok("동아리 모든 멤버 로드됨", clubMemberService.getAllClubMembers(authenticationHolder.current(), id).stream().map(ClubStudentRes::of).toList());
    }

    @Transactional(readOnly = true)
    public ResponseData<List<ClubStudentRes>> getActiveClubMembers(Long id) {
        return ResponseData.ok("동아리 모든 멤버 로드됨", clubMemberService.getActiveClubMembers(id).stream().map(ClubStudentRes::of).toList());
    }
}
