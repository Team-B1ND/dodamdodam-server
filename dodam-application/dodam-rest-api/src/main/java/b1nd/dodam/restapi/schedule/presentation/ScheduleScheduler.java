package b1nd.dodam.restapi.schedule.presentation;

import b1nd.dodam.restapi.schedule.application.ScheduleUseCase;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Slf4j
@Component
@RequiredArgsConstructor
public class ScheduleScheduler {
    private final ScheduleUseCase scheduleUseCase;

    @Scheduled(cron = "0 0 7 1 * ?", zone = "Asia/Seoul")
    public void flushAllScheduleData() {
        LocalDate now = LocalDate.now();
        LocalDate startDate = LocalDate.of(now.getYear(), 3, 2);
        LocalDate endDate = LocalDate.of(now.getYear()+1, 3, 1);
        scheduleUseCase.createScheduleByNeis(startDate, endDate);
    }
}
