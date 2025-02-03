package b1nd.dodam.domain.rds.point.event;

import java.util.List;

public interface PointSMSEvent {

    String content();

    String phone();

   List<String> parentPhones();
}
