package b1nd.dodam.neis.schedule.client.data;

import java.time.LocalDate;
import java.util.List;

public record NeisSchedule(
    String eventName,
    List<String> grades,
    LocalDate startDate,
    LocalDate endDate
) {
}
