package b1nd.dodamapi.security.filter;

import b1nd.dodamapi.security.common.ErrorResponseSender;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodaminfra.webclient.exception.WebClientException;
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
