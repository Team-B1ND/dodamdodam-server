package b1nd.dodam.domain.rds.member.service.support;

import b1nd.dodam.domain.rds.member.event.MemberIssuedEvent;

public class MemberMessageUtil {

    private MemberMessageUtil() {}

    public static MemberIssuedEvent createIssuedEvent(String phone, int authCode){
        return new MemberIssuedEvent(createMessage(authCode), phone);
    }

    public static String createMessage(int authCode) {
        return String.format("[인증번호: %s] 도담도담 인증을 진행해 주세요.", authCode);
    }

}
