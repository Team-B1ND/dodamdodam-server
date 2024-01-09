package b1nd.dodaminfra.security.token;

import b1nd.dodamcore.auth.application.dto.response.LoginResponse;
import b1nd.dodamcore.auth.application.TokenClient;
import b1nd.dodamcore.auth.application.dto.response.TokenResponse;
import b1nd.dodamcore.auth.application.dto.Token;
import b1nd.dodamcore.auth.application.dto.request.TokenRequest;
import b1nd.dodamcore.auth.application.dto.response.VerifyTokenResponse;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import b1nd.dodaminfra.webclient.WebClientSupport;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Objects;
import java.util.concurrent.CompletableFuture;

@Component
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
        VerifyTokenResponse.VerifyToken token = verifyToken(refreshToken).getData();

        if(isInvalidToken(token.getIss(), token.getSub())) {
            throw new RuntimeException();
        }

        MemberRole role = MemberRole.valueOfNumber(token.getAccessLevel());

        return issueToken(token.getMemberId(), role, tokenProperties.getGenerate()).join();
    }

    @Override
    public String getMemberIdByToken(String token) {
        VerifyTokenResponse verifyTokenRo = verifyToken(token);

        if (verifyTokenRo.getData() == null) {
            //throw ExpiredTokenException.EXCEPTION;
            throw new RuntimeException();
        }

        return verifyTokenRo.getData().getMemberId();
    }

    @Override
    public VerifyTokenResponse verifyToken(String token) {
        VerifyTokenResponse ro = webClient.post(
                        jwtProperties.getTokenServer() + tokenProperties.getVerify(),
                        new Token(token),
                        VerifyTokenResponse.class)
                .getBody();

        if(ro == null) {
            // TODO :: 에러 관리 파일에 로깅하기
            throw new RuntimeException();
            //throw TokenServerException.EXCEPTION;
        }

        switch (ro.getStatusCode()) {
            case 400 ->
                //throw CredentialsNotFoundException.EXCEPTION;
                    throw new RuntimeException();
            case 403 ->
                //throw InvalidTokenException.EXCEPTION;
                    throw new RuntimeException();
            case 410 ->
                //throw ExpiredTokenException.EXCEPTION;
                    throw new RuntimeException();
        }

        return ro;
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

    private boolean isInvalidToken(String iss, String sub) {
        return !jwtProperties.getIssuer().equals(iss) || !sub.equals("token");
    }

}