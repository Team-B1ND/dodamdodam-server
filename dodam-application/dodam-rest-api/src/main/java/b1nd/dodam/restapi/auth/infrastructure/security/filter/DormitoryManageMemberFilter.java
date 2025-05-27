package b1nd.dodam.restapi.auth.infrastructure.security.filter;

import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.domain.rds.member.repository.DormitoryManageMemberRepository;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.support.exception.ErrorResponseSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Component
@RequiredArgsConstructor
public class DormitoryManageMemberFilter extends OncePerRequestFilter {
    
    private final DormitoryManageMemberRepository repository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ErrorResponseSender errorResponseSender;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {

        final String uri = request.getRequestURI();
        final String method = request.getMethod();

        if (!uri.contains("night-study")) {
            filterChain.doFilter(request, response);
            return;
        }

        if (isStudentAccessible(uri, method)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            Member member = memberAuthenticationHolder.current();
            MemberRole role = member.getRole();

            if (MemberRole.ADMIN.equals(role) || MemberRole.TEACHER.equals(role)) {
                filterChain.doFilter(request, response);
                return;
            }

            if (MemberRole.STUDENT.equals(role) && repository.existsByMember(member)) {
                addTeacherAuthority();
                filterChain.doFilter(request, response);
                return;
            }

            errorResponseSender.send(response, GlobalExceptionCode.INVALID_ROLE);
        } catch (Exception e) {
            errorResponseSender.send(response, GlobalExceptionCode.TOKEN_NOT_PROVIDED);
        }
    }

    private void addTeacherAuthority() {
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        if (auth != null) {
            Collection<? extends GrantedAuthority> authorities = auth.getAuthorities();
            List<GrantedAuthority> updatedAuthorities = new ArrayList<>(authorities);

            SimpleGrantedAuthority teacherAuthority = new SimpleGrantedAuthority("ROLE_TEACHER");
            if (!updatedAuthorities.contains(teacherAuthority)) updatedAuthorities.add(teacherAuthority);

            Authentication newAuth = new UsernamePasswordAuthenticationToken(
                auth.getPrincipal(), auth.getCredentials(), updatedAuthorities);

            SecurityContextHolder.getContext().setAuthentication(newAuth);
        }
    }

    private boolean isStudentAccessible(String uri, String method) {
        if (GET.matches(method)) {
            return uri.equals("/night-study/my") ||
                   uri.equals("/night-study/ban/my") ||
                   uri.equals("/night-study/project/my") ||
                   uri.equals("/night-study/project/rooms") ||
                   uri.contains("/night-study/students/");
        }

        if (POST.matches(method)) return uri.equals("/night-study") || uri.equals("/night-study/project");

        if (DELETE.matches(method)) return !uri.contains("/night-study/ban");

        return false;
    }
}