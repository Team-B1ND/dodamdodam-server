package b1nd.dodam.domain.redis.fcm;

import b1nd.dodam.core.exception.global.InternalServerException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Component;

@Component
@Slf4j
@RequiredArgsConstructor
public class RedisQueueProducer {

    private final StringRedisTemplate redisTemplate;
    private final ObjectMapper objectMapper;

    private static final String KEY = "fcm:retry";


    public void enqueue(FcmRedisMessage message) {
        try {
            String json = objectMapper.writeValueAsString(message);
            redisTemplate.opsForZSet().add(KEY, json, message.retryAtMillis());

            log.warn("[FCM RETRY] 실패한 FCM 메시지를 큐에 등록합니다. retryCount={}, token={}",
                    message.retryCount(), message.token());
        } catch (Exception e) {
            log.error("[FCM RETRY] 메시지 직렬화 실패", e);
            throw new InternalServerException();
        }
    }
}
