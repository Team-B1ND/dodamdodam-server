package b1nd.dodamcore.point.domain.event;

import b1nd.dodamcore.sms.domain.event.SMSEvent;

public record PointCanceledEvent(String content, String phone) implements SMSEvent {}
