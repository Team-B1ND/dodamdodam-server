package b1nd.dodam.token.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.token.client.data.Token;
import b1nd.dodam.token.client.data.req.TokenReq;
import b1nd.dodam.token.client.data.res.TokenInfoRes;
import b1nd.dodam.token.client.data.res.TokenRes;
import b1nd.dodam.token.client.data.res.Tokens;
import b1nd.dodam.token.client.properties.DodamTokenProperties;
import b1nd.dodam.token.client.properties.JwtProperties;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
public final class DodamTokenClient {

    private final WebClientSupport webClient;
    private final JwtProperties jwtProperties;
    private final DodamTokenProperties tokenProperties;

    public CompletableFuture<Tokens> issueTokens(String id, int role) {
        CompletableFuture<String> accessToken = issueToken(id, role, tokenProperties.getGenerateAccess());
        CompletableFuture<String> refreshToken = issueToken(id, role, tokenProperties.getGenerateRefresh());

        return accessToken.thenCombine(refreshToken, Tokens::new);
    }

// TODO reissue
//    public CompletableFuture<String> reissueToken(String refreshToken) {
//        return CompletableFuture.supplyAsync(() -> verifyToken(refreshToken).data())
//                .thenCompose(data -> issueToken(data.memberId(), data.accessLevel(), tokenProperties.getGenerate()));
//    }

    public String getMemberIdByToken(String token) {
        return verifyToken(token).data().memberId();
    }

    public TokenInfoRes verifyToken(String token) {
        return webClient.post(
                jwtProperties.getTokenServer() + tokenProperties.getVerify(),
                new Token(token),
                TokenInfoRes.class
        ).block();
    }

    private CompletableFuture<String> issueToken(String userId, int role, String url) {
        return webClient.post(
                jwtProperties.getTokenServer() + url,
                new TokenReq(userId, role),
                TokenRes.class
        ).toFuture().thenApply(res -> res.data().token());
    }

}
