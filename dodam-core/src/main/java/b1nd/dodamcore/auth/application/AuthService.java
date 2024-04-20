package b1nd.dodamcore.auth.application;

import b1nd.dodamcore.auth.application.dto.req.DAuthLoginReq;
import b1nd.dodamcore.auth.application.dto.req.LoginReq;
import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.DAuthLoginRes;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.auth.application.dto.res.ReissueTokenRes;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
import b1nd.dodamcore.member.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;

@Service
@Transactional(readOnly = true)
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenClient tokenClient;
    private final PasswordEncoder passwordEncoder;
    private final Executor executor;

    public AuthService(MemberRepository memberRepository,
                       TokenClient tokenClient,
                       PasswordEncoder passwordEncoder,
                       @Qualifier("asyncExecutor") Executor executor) {
        this.memberRepository = memberRepository;
        this.tokenClient = tokenClient;
        this.passwordEncoder = passwordEncoder;
        this.executor = executor;
    }

    public CompletableFuture<LoginRes> login(LoginReq req) {
        Member member = memberRepository.findById(req.id())
                .orElseThrow(MemberNotFoundException::new);

        member.login(req.pw(), passwordEncoder);

        return CompletableFuture.supplyAsync(() -> member, executor)
                .thenComposeAsync(tokenClient::issueTokens, executor);
    }

    @Async
    public CompletableFuture<ReissueTokenRes> reissue(ReissueTokenReq req) {
        return tokenClient.reissueToken(req)
                .thenApply(ReissueTokenRes::new);
    }

    public DAuthLoginRes login(DAuthLoginReq req){
        Member member = memberRepository.findById(req.id())
                .orElseThrow(MemberNotFoundException::new);

        member.login(req.pw(), passwordEncoder);
        return new DAuthLoginRes(member);
    }
}