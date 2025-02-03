package b1nd.dodam.domain.rds.point.event;

import java.util.List;

public record PointIssuedEvent(String content, String phone, List<String> parentPhones) implements PointSMSEvent {}
