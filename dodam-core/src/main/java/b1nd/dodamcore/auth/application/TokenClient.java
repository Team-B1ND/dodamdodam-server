package b1nd.dodamcore.auth.application;

import b1nd.dodamcore.auth.application.dto.response.TokenInfoResponse;
import b1nd.dodamcore.auth.application.dto.response.LoginResponse;
import b1nd.dodamcore.member.domain.entity.Member;

import java.util.concurrent.CompletableFuture;

public interface TokenClient {

    CompletableFuture<LoginResponse> issueTokens(Member member);

    TokenInfoResponse verifyToken(String token);

    String reissueAccessToken(String refreshToken);

    String getMemberIdByToken(String token);

}