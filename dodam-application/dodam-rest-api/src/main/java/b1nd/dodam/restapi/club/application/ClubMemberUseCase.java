package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.res.ClubMemberRes;
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

    public ResponseData<List<ClubMemberRes>> getClubJoinRequestsReceived() {
        return ResponseData.ok("받은 부원 제안", clubMemberService.getJoinRequests(authenticationHolder.current()).stream().map(ClubMemberRes::of).toList());
    }
}
