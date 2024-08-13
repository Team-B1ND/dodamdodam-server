package b1nd.dodam.restapi.support.meal;

import b1nd.dodam.domain.redis.meal.service.MealCacheService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Component
@Slf4j
@RequiredArgsConstructor
public class MealCacheScheduler {

    private final MealCacheService mealCacheService;

    @Scheduled(cron = "0 0 7 1 * ?", zone = "Asia/Seoul")
    public void flushAllMealData() {
        try {
            log.info("<<<<<<<<<< flushMealData Start {} >>>>>>>>>>", ZonedDateTime.now());
            mealCacheService.evictAll();
        } catch (Exception e) {
            log.error("fail to process", e);
        }
    }

}
