package b1nd.dodamcore.auth.application;

import b1nd.dodamcore.auth.application.dto.req.LoginReq;
import b1nd.dodamcore.auth.application.dto.req.ReissueTokenReq;
import b1nd.dodamcore.auth.application.dto.res.LoginRes;
import b1nd.dodamcore.auth.application.dto.res.ReissueTokenRes;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.exception.MemberNotFoundException;
import b1nd.dodamcore.member.repository.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.concurrent.CompletableFuture;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class AuthService {

    private final MemberRepository memberRepository;
    private final TokenClient tokenClient;
    private final PasswordEncoder passwordEncoder;

    public CompletableFuture<LoginRes> login(LoginReq req) {
        Member member = memberRepository.findById(req.id())
                .orElseThrow(MemberNotFoundException::new);

        member.login(req.pw(), passwordEncoder);

        return CompletableFuture.supplyAsync(() -> member)
                .thenCompose(tokenClient::issueTokens);
    }

    @Async
    public CompletableFuture<ReissueTokenRes> reissue(ReissueTokenReq req) {
        return tokenClient.reissueToken(req)
                .thenApply(ReissueTokenRes::new);
    }

}