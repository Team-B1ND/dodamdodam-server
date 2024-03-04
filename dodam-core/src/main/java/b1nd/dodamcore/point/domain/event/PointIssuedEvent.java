package b1nd.dodamcore.point.domain.event;

import b1nd.dodamcore.sms.domain.event.SMSEvent;

public record PointIssuedEvent(String content, String phone) implements SMSEvent {}