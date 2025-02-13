package b1nd.dodam.domain.rds.point.event;

public record PointCanceledEvent(String content, String phone) implements PointSMSEvent {}
