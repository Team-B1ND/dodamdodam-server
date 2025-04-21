package b1nd.dodam.domain.redis.fcm;

import b1nd.dodam.core.exception.global.InternalServerException;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@Slf4j
@RequiredArgsConstructor
public class FCMRedisQueueProducer {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String RETRY_QUEUE_KEY = "fcm:retry";
    private static final String FAILED_QUEUE_KEY = "fcm:failed";

    public void enqueueToRetryQueue(FcmRedisMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForZSet().add(RETRY_QUEUE_KEY, json, message.retryAtMillis());

            log.warn("[FCM RETRY] 실패한 FCM 메시지를 큐에 등록합니다. retryCount={}, token={}",
                    message.retryCount(), message.token());
        } catch (Exception e) {
            log.error("[FCM RETRY] 메시지 직렬화 실패", e);
            throw new InternalServerException();
        }
    }

    public FcmRedisMessage dequeueToRetryQueue(String msg) {
        redisTemplate.opsForZSet().remove(RETRY_QUEUE_KEY, msg);
        try {
            return objectMapper.readValue(msg, FcmRedisMessage.class);
        } catch (JsonProcessingException e) {
            return null;
        }
    }

    public Set<String> getMessagesFromRetryQueue(long now) {
        return redisTemplate.opsForZSet().rangeByScore(RETRY_QUEUE_KEY, 0, now);
    }

    public void enqueueToFailedQueue(FcmRedisMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForZSet().add(FAILED_QUEUE_KEY, json, message.retryAtMillis());
        } catch (Exception e) {
            throw new InternalServerException();
        }
    }
}
