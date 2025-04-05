package b1nd.dodam.token.client;

import lombok.RequiredArgsConstructor;
import java.util.concurrent.CompletableFuture;
import org.springframework.stereotype.Component;

import b1nd.dodam.token.client.data.Token;
import b1nd.dodam.token.client.data.res.Tokens;
import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.token.client.data.req.TokenReq;
import b1nd.dodam.token.client.data.res.TokenRes;
import b1nd.dodam.token.client.data.req.ReissueReq;
import b1nd.dodam.token.client.data.res.TokenInfoRes;
import b1nd.dodam.token.client.properties.JwtProperties;
import b1nd.dodam.token.client.properties.DodamTokenProperties;

@Component
@RequiredArgsConstructor
public final class DodamTokenClient {

    private final WebClientSupport webClient;
    private final JwtProperties jwtProperties;
    private final DodamTokenProperties tokenProperties;

    public CompletableFuture<Tokens> issueTokens(String id, int role) {
        CompletableFuture<String> accessToken = requestIssueToken(id, role, tokenProperties.getGenerateAccess());
        CompletableFuture<String> refreshToken = requestIssueToken(id, role, tokenProperties.getGenerateRefresh());

        return accessToken.thenCombine(refreshToken, Tokens::new);
    }

    public CompletableFuture<String> reissueToken(String refreshToken) {
        return requestReissueToken(refreshToken);
    }

    public String getMemberIdByToken(String token) {
        return requestVerifyToken(token).data().memberId();
    }

    private TokenInfoRes requestVerifyToken(String token) {
        return webClient.post(
            jwtProperties.getTokenServer() + tokenProperties.getVerify(),
            new Token(token),
            TokenInfoRes.class
        ).block();
    }

    private CompletableFuture<String> requestReissueToken(String refreshToken) {
        return webClient.post(
            jwtProperties.getTokenServer() + tokenProperties.getReissueAccess(),
            new ReissueReq(refreshToken),
            TokenRes.class
        ).toFuture().thenApply(res -> res.data().token());
    }

    private CompletableFuture<String> requestIssueToken(String userId, int role, String url) {
        return webClient.post(
            jwtProperties.getTokenServer() + url,
            new TokenReq(userId, role),
            TokenRes.class
        ).toFuture().thenApply(res -> res.data().token());
    }

}
