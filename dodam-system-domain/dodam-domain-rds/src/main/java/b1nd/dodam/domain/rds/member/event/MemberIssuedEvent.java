package b1nd.dodam.domain.rds.member.event;

public record MemberIssuedEvent(String content, String phone) implements MemberSMSEvent{
}
