package b1nd.dodamapi.auth.usecase;

import b1nd.dodamapi.common.response.ResponseData;
import b1nd.dodamcore.auth.application.PasswordEncoder;
import b1nd.dodamcore.auth.application.TokenClient;
import b1nd.dodamcore.auth.application.dto.req.LoginReq;
import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.auth.application.dto.res.ReissueTokenRes;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class AuthUseCase {

    private final MemberService memberService;
    private final PasswordEncoder passwordEncoder;
    private final TokenClient tokenClient;
    private final Executor executor;

    public AuthUseCase(MemberService memberService,
                       PasswordEncoder passwordEncoder,
                       TokenClient tokenClient,
                       @Qualifier("asyncExecutor") Executor executor) {
        this.memberService = memberService;
        this.passwordEncoder = passwordEncoder;
        this.tokenClient = tokenClient;
        this.executor = executor;
    }

    public CompletableFuture<ResponseData<LoginRes>> login(LoginReq req) {
        Member member = memberService.getMemberBy(req.id());
        member.login(req.pw(), passwordEncoder);

        return CompletableFuture.supplyAsync(() -> member, executor)
                .thenCompose(tokenClient::issueTokens)
                .thenApply(res -> ResponseData.ok("로그인 성공", res));
    }

    @Async
    public CompletableFuture<ResponseData<ReissueTokenRes>> reissue(ReissueTokenReq req) {
        return tokenClient.reissueToken(req)
                .thenApply(ReissueTokenRes::new)
                .thenApply(res -> ResponseData.ok("토큰 재발급 성공", res));
    }

}
