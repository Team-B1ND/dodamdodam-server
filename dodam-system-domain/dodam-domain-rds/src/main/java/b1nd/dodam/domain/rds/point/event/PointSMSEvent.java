package b1nd.dodam.domain.rds.point.event;

public interface PointSMSEvent {

    String content();

    String phone();

   String parentPhone();
}
