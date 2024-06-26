package b1nd.dodaminfra.api.dodamtoken;

import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.auth.application.TokenClient;
import b1nd.dodamcore.auth.application.dto.res.TokenInfoRes;
import b1nd.dodamcore.auth.application.dto.res.TokenRes;
import b1nd.dodamcore.auth.application.dto.Token;
import b1nd.dodamcore.auth.application.dto.req.TokenReq;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
final class DodamTokenClient implements TokenClient {

    private final WebClientSupport webClient;
    private final JwtProperties jwtProperties;
    private final DodamTokenProperties tokenProperties;

    @Override
    public CompletableFuture<LoginRes> issueTokens(Member member) {
        CompletableFuture<String> accessToken = issueToken(member.getId(), member.getRole(), tokenProperties.getGenerate());
        CompletableFuture<String> refreshToken = issueToken(member.getId(), member.getRole(), tokenProperties.getRefresh());

        return accessToken.thenCombine(refreshToken, (a, r) -> new LoginRes(member, a, r));
    }

    @Override
    public CompletableFuture<String> reissueToken(ReissueTokenReq req) {
        return CompletableFuture.supplyAsync(() -> verifyToken(req.refreshToken()).data())
                .thenCompose(data -> issueToken(data.memberId(), MemberRole.of(data.accessLevel()), tokenProperties.getGenerate()));
    }

    @Override
    public String getMemberIdByToken(String token) {
        return verifyToken(token).data().memberId();
    }

    @Override
    public TokenInfoRes verifyToken(String token) {
        return webClient.post(
                jwtProperties.getTokenServer() + tokenProperties.getVerify(),
                new Token(token),
                TokenInfoRes.class
        ).block();
    }

    private CompletableFuture<String> issueToken(String userId, MemberRole role, String url) {
        return webClient.post(
                jwtProperties.getTokenServer() + url,
                new TokenReq(userId, role.getNumber(), 0),
                TokenRes.class
        ).toFuture().thenApply(res -> res.data().token());
    }

}
