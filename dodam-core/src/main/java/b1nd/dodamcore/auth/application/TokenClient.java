package b1nd.dodamcore.auth.application;

import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.TokenInfoRes;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.member.domain.entity.Member;

import java.util.concurrent.CompletableFuture;

public interface TokenClient {

    CompletableFuture<LoginRes> issueTokens(Member member);

    TokenInfoRes verifyToken(String token);

    CompletableFuture<String> reissueToken(ReissueTokenReq req);

    String getMemberIdByToken(String token);

}