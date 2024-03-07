package b1nd.dodaminfra.token;

import b1nd.dodamcore.common.exception.ErrorResponseEntity;
import b1nd.dodamcore.common.exception.ExceptionCode;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodaminfra.webclient.exception.WebClientException;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class TokenExceptionFilter extends OncePerRequestFilter {

    private final ObjectMapper objectMapper;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) {
        try {
            filterChain.doFilter(request, response);
        } catch (WebClientException e) {
            setErrorResponse(response, e.getExceptionCode());
        } catch (Exception e) {
            setErrorResponse(response, GlobalExceptionCode.INTERNAL_SERVER);
        }
    }

    private void setErrorResponse(HttpServletResponse response, ExceptionCode code) {
        try {
            responseToClient(
                    response,
                    ErrorResponseEntity.builder()
                            .status(code.getHttpStatus().value())
                            .code(code.getExceptionName())
                            .message(code.getMessage())
                            .build()
            );
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void responseToClient(HttpServletResponse response, ErrorResponseEntity entity) throws IOException {
        response.setStatus(entity.getStatus());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        response.setCharacterEncoding("UTF-8");
        response.getWriter().write(objectMapper.writeValueAsString(entity));
    }

}