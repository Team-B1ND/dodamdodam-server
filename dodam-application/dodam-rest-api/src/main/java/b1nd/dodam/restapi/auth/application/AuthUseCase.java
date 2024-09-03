package b1nd.dodam.restapi.auth.application;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.repository.MemberRepository;
import b1nd.dodam.restapi.auth.application.data.req.LoginReq;
import b1nd.dodam.restapi.support.data.ResponseData;
import b1nd.dodam.restapi.support.encrypt.Sha512PasswordEncoder;
import b1nd.dodam.token.client.DodamTokenClient;
import b1nd.dodam.restapi.auth.application.data.req.ReissueTokenReq;
import b1nd.dodam.restapi.auth.application.data.res.LoginRes;
import b1nd.dodam.restapi.auth.application.data.res.ReissueTokenRes;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Component
public class AuthUseCase {

    private final MemberRepository memberRepository;
    private final DodamTokenClient tokenClient;
    private final Executor executor;

    public AuthUseCase(MemberRepository memberRepository,
                       DodamTokenClient tokenClient,
                       @Qualifier("asyncExecutor") Executor executor) {
        this.memberRepository = memberRepository;
        this.tokenClient = tokenClient;
        this.executor = executor;
    }

    public CompletableFuture<ResponseData<LoginRes>> login(LoginReq req) {
        Member member = memberRepository.getById(req.id());
        member.checkIfPasswordIsCorrect(Sha512PasswordEncoder.encode(req.pw()));
        member.checkIfStatusIsDeactivate();
        return CompletableFuture.supplyAsync(() -> member, executor)
                .thenCompose(m -> tokenClient.issueTokens(member.getId(), member.getRole().getNumber()))
                .thenApply(tokens -> new LoginRes(member, tokens.accessToken(), tokens.refreshToken()))
                .thenApply(res -> ResponseData.ok("로그인 성공", res));
    }

    @Async
    public CompletableFuture<ResponseData<ReissueTokenRes>> reissue(ReissueTokenReq req) {
        return tokenClient.reissueToken(req.refreshToken())
                .thenApply(ReissueTokenRes::new)
                .thenApply(res -> ResponseData.ok("토큰 재발급 성공", res));
    }

}
