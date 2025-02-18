package b1nd.dodam.restapi.club.application;

import b1nd.dodam.domain.rds.club.entity.Club;
import b1nd.dodam.domain.rds.club.entity.ClubMember;
import b1nd.dodam.domain.rds.club.service.ClubMemberService;
import b1nd.dodam.domain.rds.club.service.ClubService;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.club.application.data.req.CreateClubReq;
import b1nd.dodam.restapi.support.data.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Component
@RequiredArgsConstructor
@Transactional(rollbackFor = Exception.class)
public class ClubUseCase {
    private final ClubService clubService;
    private final ClubMemberService clubMemberService;
    private final MemberRepository memberRepository;
    private final MemberAuthenticationHolder authHolder;

    public Response save(CreateClubReq req, List<String>memberIdList) {
        clubService.checkIsNameDuplicated(req.name());
        Club club = req.toEntity();
        clubService.save(club);
        clubMemberService.saveOwner(club, authHolder.current());

        clubMemberService.saveWithBuild(club, memberIdList.stream().map(memberRepository::getById).toList(), 1,);


        return Response.created("동아리 생성 완료");
    }
}
