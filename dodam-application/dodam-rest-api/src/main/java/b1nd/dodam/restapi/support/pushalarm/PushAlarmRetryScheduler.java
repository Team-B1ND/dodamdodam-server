package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.core.exception.global.InternalServerException;
import b1nd.dodam.domain.redis.fcm.FcmRedisMessage;
import b1nd.dodam.process.listener.pushalarm.FcmEvent;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
public class PushAlarmRetryScheduler {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;
    private final ApplicationEventPublisher publisher;

    private static final String KEY = "fcm:retry";

    public PushAlarmRetryScheduler(StringRedisTemplate redisTemplate,
                             ObjectMapper objectMapper,
                             ApplicationEventPublisher publisher) {
        this.redisTemplate = redisTemplate;
        this.objectMapper = objectMapper;
        this.publisher = publisher;
    }

    @Scheduled(fixedRate = 3000)
    public void processRetryQueue() {
        long now = System.currentTimeMillis();
        Set<String> messages = redisTemplate.opsForZSet().rangeByScore(KEY, 0, now);

        if (messages == null || messages.isEmpty()) return;

        for (String msg : messages) {
            try {
                FcmRedisMessage redisMessage = objectMapper.readValue(msg, FcmRedisMessage.class);
                publisher.publishEvent(FcmEvent.from(redisMessage));
                redisTemplate.opsForZSet().remove(KEY, msg);
            } catch (Exception e) {
                throw new InternalServerException();
            }
        }
    }
}
