package b1nd.dodam.neis.schedule.client;

import b1nd.dodam.client.core.WebClientSupport;
import b1nd.dodam.neis.client.core.NeisCoreProperties;
import b1nd.dodam.neis.schedule.client.data.NeisSchedule;
import b1nd.dodam.neis.schedule.client.properties.NeisScheduleProperties;
import b1nd.dodam.neis.schedule.client.util.NeisScheduleUtil;
import lombok.RequiredArgsConstructor;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.springframework.stereotype.Component;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class NeisScheduleClient {
    private final WebClientSupport webClient;
    private final NeisCoreProperties coreProperties;
    private final NeisScheduleProperties scheduleProperties;

    public List<NeisSchedule> getSchedules(LocalDate startDate, LocalDate endDate) {
        String rawResponse = getRawSchedule(startDate, endDate);
        JSONArray rawData = NeisScheduleUtil.getRawData(rawResponse);
        List<NeisSchedule> scheduleList = new ArrayList<>();

        for (Object object : rawData) {
            JSONObject scheduleData = (JSONObject) object;

            String eventName = String.valueOf(scheduleData.get("EVENT_NM"));
            List<String> grades = NeisScheduleUtil.determineGrades(scheduleData);
            LocalDate date = NeisScheduleUtil.parseDate(String.valueOf(scheduleData.get("AA_YMD")));

            scheduleList.add(new NeisSchedule(eventName, grades, date, null));
        }

        return mergeSchedules(scheduleList);
    }

    private List<NeisSchedule> mergeSchedules(List<NeisSchedule> scheduleList) {
        return scheduleList.stream()
                .collect(Collectors.groupingBy(NeisSchedule::eventName))
                .entrySet().stream()
                .map(entry -> createMergedSchedule(entry.getKey(), entry.getValue()))
                .collect(Collectors.toList());
    }

    private NeisSchedule createMergedSchedule(String eventName, List<NeisSchedule> schedules) {
        LocalDate minDate = schedules.stream()
                .map(NeisSchedule::startDate)
                .min(LocalDate::compareTo)
                .orElse(null);

        LocalDate maxDate = schedules.stream()
                .map(NeisSchedule::startDate)
                .max(LocalDate::compareTo)
                .orElse(null);

        List<String> mergedGrades = schedules.get(0).grades();
        return new NeisSchedule(eventName, mergedGrades, minDate, maxDate);
    }

    private String getRawSchedule(LocalDate startDate, LocalDate endDate) {
        return webClient.get(
                UriComponentsBuilder.fromUriString(coreProperties.getUrl() + scheduleProperties.getScheduleEndpoint())
                        .build(coreProperties.getApiKey(), startDate, endDate).toString(),
                String.class
        ).block();
    }
}
