package b1nd.dodam.restapi.auth.infrastructure.security.filter;

import b1nd.dodam.core.exception.global.GlobalExceptionCode;
import b1nd.dodam.domain.rds.division.entity.Division;
import b1nd.dodam.domain.rds.division.entity.DivisionMember;
import b1nd.dodam.domain.rds.division.enumeration.DivisionPermission;
import b1nd.dodam.domain.rds.division.service.DivisionMemberService;
import b1nd.dodam.domain.rds.division.service.DivisionService;
import b1nd.dodam.domain.rds.member.entity.Member;
import b1nd.dodam.domain.rds.member.enumeration.MemberRole;
import b1nd.dodam.restapi.auth.infrastructure.security.support.MemberAuthenticationHolder;
import b1nd.dodam.restapi.support.exception.ErrorResponseSender;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.HandlerMapping;

import java.util.Map;

import static org.springframework.http.HttpMethod.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class DivisionPermissionInterceptor implements HandlerInterceptor {
    private final MemberAuthenticationHolder memberAuthenticationHolder;
    private final ErrorResponseSender errorResponseSender;
    private final DivisionService divisionService;
    private final DivisionMemberService divisionMemberService;

    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        final String method = request.getMethod();
        Member member = memberAuthenticationHolder.current();
        if (isAdminOrTeacher(member)) return true;
        if (isGetHttpMethod(method)) return true;
        Map<String, String> pathVariables = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
        Long id = Long.valueOf(pathVariables.get("id"));
        Division division = divisionService.getById(id);
        DivisionMember divisionMember = divisionMemberService.getByDivisionAndMember(division, member);
        if (divisionMember == null) {
            errorResponseSender.send(response, GlobalExceptionCode.INVALID_PERMISSION);
            return false;
        }
        if(divisionMember.getPermission() == DivisionPermission.READER){
            errorResponseSender.send(response, GlobalExceptionCode.INVALID_PERMISSION);
            return false;
        }
        return true;
    }

    private boolean isGetHttpMethod(String method) {
        return GET.matches(method);
    }

    private boolean isAdminOrTeacher(Member member) {
        return member.getRole() == MemberRole.ADMIN || member.getRole() == MemberRole.TEACHER;
    }
}
