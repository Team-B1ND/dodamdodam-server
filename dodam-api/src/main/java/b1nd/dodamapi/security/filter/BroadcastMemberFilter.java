package b1nd.dodamapi.security.filter;

import b1nd.dodamapi.security.common.ErrorResponseSender;
import b1nd.dodamcore.common.exception.GlobalExceptionCode;
import b1nd.dodamcore.member.application.MemberService;
import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import b1nd.dodamcore.member.domain.enums.MemberRole;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

import static org.springframework.http.HttpMethod.*;

@Component
@RequiredArgsConstructor
public class BroadcastMemberFilter extends OncePerRequestFilter {

    private final MemberService memberService;
    private final MemberSessionHolder memberSessionHolder;
    private final ErrorResponseSender errorResponseSender;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
        final String uri = request.getRequestURI();

        if(isWakeupSongEndpoint(uri)) {
            final String method = request.getMethod();
            final Member member;

            if (needsMemberDetails(uri)) {
                try {
                    memberSessionHolder.current();
                } catch (Exception e) {
                    errorResponseSender.send(response, GlobalExceptionCode.TOKEN_NOT_PROVIDED);
                    return ;
                }
            }

            if (needsBroadcastClubMemberCheck(uri, method)) {
                try {
                    member = memberSessionHolder.current();
                } catch (Exception e) {
                    errorResponseSender.send(response, GlobalExceptionCode.TOKEN_NOT_PROVIDED);
                    return ;
                }

                if(isTeacher(member.getRole())) {
                    errorResponseSender.send(response, GlobalExceptionCode.INVALID_ROLE);
                    return ;
                }

                if(isNotBroadcastClubMemberAndAdmin(member)) {
                    errorResponseSender.send(response, GlobalExceptionCode.INVALID_ROLE);
                    return ;
                }
            }
        }

        filterChain.doFilter(request, response);
    }

    private boolean isWakeupSongEndpoint(String uri) {
        return uri.contains("wakeup-song");
    }

    private boolean isTeacher(MemberRole role) {
        return MemberRole.TEACHER.equals(role);
    }

    private boolean needsMemberDetails(String uri) {
        return uri.contains("my");
    }

    private boolean needsBroadcastClubMemberCheck(String uri, String method) {
        return !uri.contains("my") && (PATCH.matches(method) || DELETE.matches(method));
    }

    private boolean isNotBroadcastClubMemberAndAdmin(Member member) {
        return !MemberRole.ADMIN.equals(member.getRole()) && !memberService.checkBroadcastClubMember(member);
    }

}
