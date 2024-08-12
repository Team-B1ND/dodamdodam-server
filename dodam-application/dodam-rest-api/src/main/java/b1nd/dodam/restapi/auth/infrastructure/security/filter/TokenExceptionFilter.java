package b1nd.dodam.restapi.auth.infrastructure.security.filter;

import b1nd.dodam.client.core.exception.WebClientException;
import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.restapi.support.exception.ErrorResponseSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

@Component
@RequiredArgsConstructor
public class TokenExceptionFilter extends OncePerRequestFilter {

    private final ErrorResponseSender errorResponseSender;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (WebClientException e) {
            errorResponseSender.send(response, e.getExceptionCode());
        } catch (Exception e) {
            errorResponseSender.send(response, GlobalExceptionCode.INTERNAL_SERVER);
        }
    }

}
