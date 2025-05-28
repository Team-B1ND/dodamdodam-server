package b1nd.dodam.restapi.auth.infrastructure.security.filter;

import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.restapi.support.exception.ErrorResponseSender;
import b1nd.dodam.domain.rds.member.repository.DormitoryManageMemberRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
@RequiredArgsConstructor
public class DormitoryManageMemberFilter extends OncePerRequestFilter {

    private final ErrorResponseSender errorResponseSender;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final DormitoryManageMemberRepository dormitoryManageMemberRepository;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        String uri = request.getRequestURI();
        String method = request.getMethod();

        if (!uri.contains("night-study")) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Member member = memberAuthenticationHolder.current();

            if (isPublicEndpoint(uri, method) ||
                    isStudentAllowed(uri, method, member) ||
                    hasNightStudyManageAccess(member)) {
                filterChain.doFilter(request, response);
            } else {
                errorResponseSender.send(response, GlobalExceptionCode.INVALID_ROLE);
            }
        } catch (Exception e) {
            errorResponseSender.send(response, GlobalExceptionCode.TOKEN_NOT_PROVIDED);
        }
    }

    private boolean isStudentAllowed(String uri, String method, Member member) {
        if (member.getRole() != MemberRole.STUDENT) return false;
        return switch (method) {
            case "POST" -> uri.equals("/night-study") || uri.equals("/night-study/project");
            case "DELETE" -> !uri.contains("/night-study/ban");
            case "GET" -> uri.equals("/night-study/my") || uri.equals("/night-study/ban/my") || uri.equals("/night-study/project/my");
            default -> false;
        };
    }

    private boolean isPublicEndpoint(String uri, String method) {
        return "GET".equals(method) &&
                (uri.equals("/night-study/project/rooms") || uri.contains("/night-study/students/"));
    }

    private boolean hasNightStudyManageAccess(Member member) {
        MemberRole role = member.getRole();
        return role == MemberRole.ADMIN ||
                role == MemberRole.TEACHER ||
                (role == MemberRole.STUDENT && dormitoryManageMemberRepository.existsByMember(member));
    }
}