package b1nd.dodamcore.auth.application;

import b1nd.dodamcore.auth.application.dto.request.LoginRequest;
import b1nd.dodamcore.auth.application.dto.response.LoginResponse;
import b1nd.dodamcore.member.domain.entity.Member;
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

    @Async
    public CompletableFuture<LoginResponse> login(LoginRequest request) {
        CompletableFuture<Member> member = CompletableFuture.supplyAsync(
                () -> memberRepository.findById(request.id())
                        .orElseThrow(RuntimeException::new)
                ).thenApply(m -> m.login(request.pw(), passwordEncoder));

        return member.thenCompose(tokenClient::issueTokens);
    }

}