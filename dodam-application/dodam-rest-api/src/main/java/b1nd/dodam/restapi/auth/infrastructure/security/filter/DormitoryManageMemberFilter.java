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
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;

import static org.springframework.http.HttpMethod.*;

@Component
@RequiredArgsConstructor
public class DormitoryManageMemberFilter extends OncePerRequestFilter {
    
    private final DormitoryManageMemberRepository repository;
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ErrorResponseSender errorResponseSender;
    
    private static final List<String> STUDENT_PATHS = Arrays.asList(
        "/night-study/my", 
        "/night-study/ban/my", 
        "/night-study/project/my",
        "/night-study/project/rooms", 
        "/night-study/students/",
        "/night-study",
        "/night-study/project"
    );
    
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String uri = request.getRequestURI();
        if (!uri.contains("night-study")) {
            filterChain.doFilter(request, response);
            return;
        }
        
        if (isStudentAccessible(uri, request.getMethod())) {
            filterChain.doFilter(request, response);
            return;
        }
        
        Member member;
        try {
            member = memberAuthenticationHolder.current();
        } catch (Exception e) {
            errorResponseSender.send(response, GlobalExceptionCode.TOKEN_NOT_PROVIDED);
            return;
        }
        
        if (MemberRole.ADMIN.equals(member.getRole()) ||
            MemberRole.TEACHER.equals(member.getRole()) ||
            (MemberRole.STUDENT.equals(member.getRole()) && repository.existsByMember(member))) {
            filterChain.doFilter(request, response);
            return;
        }
        
        errorResponseSender.send(response, GlobalExceptionCode.INVALID_ROLE);
    }

    private boolean isStudentAccessible(String uri, String method) {
        for (String path : STUDENT_PATHS) {
            if (uri.contains(path)) return true;
        }
        
        return DELETE.matches(method) && !uri.contains("/night-study/ban");
    }
}