package b1nd.dodaminfra.token;

import b1nd.dodamcore.auth.application.TokenClient;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodaminfra.security.common.MemberDetails;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenFilter extends OncePerRequestFilter {

    private static final String TOKEN_TYPE = "Bearer";

    private final TokenClient tokenClient;
    private final MemberService memberService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String token = TokenExtractor.extract(request, TOKEN_TYPE);

        if (!token.isEmpty()) {
            String memberId = tokenClient.getMemberIdByToken(token);
            Member member = memberService.getById(memberId);
            MemberDetails details = new MemberDetails(member);

            Authentication authentication = new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authentication);
        }

        filterChain.doFilter(request, response);
    }

}