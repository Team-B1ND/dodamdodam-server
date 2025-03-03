package b1nd.dodam.restapi.auth.infrastructure.security.support;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.restapi.auth.infrastructure.security.MemberDetails;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
public final class MemberAuthenticationHolder {

    public Member current() {
        return ((MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).member();
    }

    public static String getUserAgent(HttpServletRequest request) {
        String userAgent = request.getHeader("User-Agent");
        return userAgent;
    }

}
