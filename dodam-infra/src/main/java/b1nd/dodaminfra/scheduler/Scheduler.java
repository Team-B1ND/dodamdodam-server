package b1nd.dodaminfra.scheduler;

import b1nd.dodaminfra.redis.RedisUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.ZonedDateTime;

@Slf4j
@Component
@RequiredArgsConstructor
public class Scheduler {

    private final RedisUtil redisUtil;

    @Scheduled(cron = "0 0 7 1 * ?", zone = "Asia/Seoul")
    public void setAttendanceJob() {
        try {
            log.info("<<<<<<<<<< flushMealData Start {} >>>>>>>>>>", ZonedDateTime.now());
            redisUtil.flushAllMealData();
        } catch (Exception e) {
            log.error("fail to process", e);
        }
    }
}
