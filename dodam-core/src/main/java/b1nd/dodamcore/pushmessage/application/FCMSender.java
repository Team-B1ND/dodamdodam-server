package b1nd.dodamcore.pushmessage.application;

import b1nd.dodamcore.member.domain.entity.Member;

import java.util.List;

public interface FCMSender {
    void sendByMemberList(List<Member> memberList, String title, String body);
}
