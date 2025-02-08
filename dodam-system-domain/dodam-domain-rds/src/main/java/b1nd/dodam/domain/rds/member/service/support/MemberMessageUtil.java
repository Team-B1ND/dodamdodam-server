package b1nd.dodam.domain.rds.member.service.support;

import b1nd.dodam.domain.rds.member.event.MemberIssuedEvent;

public class MemberMessageUtil {

    private MemberMessageUtil() {}

    public static MemberIssuedEvent createIssuedEvent(String phone, int authCode){
        return new MemberIssuedEvent(createMessage(authCode), phone);
    }

    private static String createMessage(int authCode) {
        return String.format("[인증번호: %s] 대구SW고 인증번호입니다.", authCode);
    }

}
