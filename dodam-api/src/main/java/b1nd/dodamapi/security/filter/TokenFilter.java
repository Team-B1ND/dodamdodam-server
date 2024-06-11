package b1nd.dodamapi.security.filter;

import b1nd.dodamapi.security.common.MemberDetails;
import b1nd.dodamapi.security.util.TokenExtractor;
import b1nd.dodamcore.auth.application.TokenClient;
import b1nd.dodamcore.member.application.MemberService;
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
        if(!token.isEmpty()) {
            setAuthentication(token);
        }
        filterChain.doFilter(request, response);
    }

    private void setAuthentication(String token) {
        SecurityContextHolder.getContext().setAuthentication(
                createAuthentication(token)
        );
    }

    private Authentication createAuthentication(String token) {
        MemberDetails details = getMemberDetails(token);
        return new UsernamePasswordAuthenticationToken(details, null, details.getAuthorities());
    }

    private MemberDetails getMemberDetails(String token) {
        String id = tokenClient.getMemberIdByToken(token);
        return new MemberDetails(memberService.getMemberBy(id));
    }

}
