package b1nd.dodam.domain.rds.point.event;

public record PointIssuedEvent(String content, String phone) implements PointSMSEvent {}
