package b1nd.dodaminfra.security.common;

import b1nd.dodamcore.member.application.MemberSessionHolder;
import b1nd.dodamcore.member.domain.entity.Member;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

@Component
final class MemberAuthenticationHolder implements MemberSessionHolder {

    @Override
    public Member current() {
        return ((MemberDetails) SecurityContextHolder.getContext().getAuthentication().getPrincipal()).member();
    }

}