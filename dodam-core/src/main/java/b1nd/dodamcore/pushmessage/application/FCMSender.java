package b1nd.dodamcore.pushmessage.application;

import b1nd.dodamcore.member.domain.entity.Member;

import java.util.List;

public interface FCMSender {
    void sendToMemberList(List<Member> memberList, String title, String body);
    void sendToMember(Member member, String title, String body);
}
