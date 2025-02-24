package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.enumeration.ClubStatus;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.res.ClubMemberRes;
import b1nd.dodam.restapi.support.data.Response;
import b1nd.dodam.restapi.support.data.ResponseData;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.PathVariable;

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

    public ResponseData<List<ClubMemberRes>> getClubJoinRequestsReceived() {
        return ResponseData.ok("받은 부원 제안", clubMemberService.getJoinRequests(authenticationHolder.current()).stream().map(ClubMemberRes::of).toList());
    }
}
