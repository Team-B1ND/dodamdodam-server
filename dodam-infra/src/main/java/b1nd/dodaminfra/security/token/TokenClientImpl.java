package b1nd.dodaminfra.security.token;

import b1nd.dodamcore.auth.application.dto.response.LoginResponse;
import b1nd.dodamcore.auth.application.TokenClient;
import b1nd.dodamcore.auth.application.dto.response.TokenInfoResponse;
import b1nd.dodamcore.auth.application.dto.response.TokenResponse;
import b1nd.dodamcore.auth.application.dto.Token;
import b1nd.dodamcore.auth.application.dto.request.TokenRequest;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
@RequiredArgsConstructor
final class TokenClientImpl implements TokenClient {

    private final WebClientSupport webClient;
    private final JwtProperties jwtProperties;
    private final TokenProperties tokenProperties;

    @Override
    public CompletableFuture<LoginResponse> issueTokens(Member member) {
        CompletableFuture<String> accessToken = issueToken(member.getId(), member.getRole(), tokenProperties.getGenerate());
        CompletableFuture<String> refreshToken = issueToken(member.getId(), member.getRole(), tokenProperties.getRefresh());

        return accessToken.thenCombine(refreshToken, (a, r) -> new LoginResponse(member, a, r));
    }

    @Override
    public String reissueAccessToken(String refreshToken) {
        TokenInfoResponse.TokenInfo token = verifyToken(refreshToken).data();

        MemberRole role = MemberRole.of(token.accessLevel());

        return issueToken(token.memberId(), role, tokenProperties.getGenerate()).join();
    }

    @Override
    public String getMemberIdByToken(String token) {
        return verifyToken(token).data().memberId();
    }

    @Override
    public TokenInfoResponse verifyToken(String token) {
        return webClient.post(
                jwtProperties.getTokenServer() + tokenProperties.getVerify(),
                        new Token(token),
                TokenInfoResponse.class
                ).getBody();
    }

    private CompletableFuture<String> issueToken(String userId, MemberRole role, String url) {
        return CompletableFuture.supplyAsync(
                () -> Objects.requireNonNull(
                        webClient.post(
                                jwtProperties.getTokenServer() + url,
                                new TokenRequest(userId, role.getNumber(), 0),
                                TokenResponse.class
                        ).getBody()
                ).data().token()
        );
    }

}