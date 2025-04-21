package b1nd.dodam.process.listener.pushalarm;

import b1nd.dodam.domain.redis.fcm.FCMRedisQueueProducer;
import b1nd.dodam.domain.redis.fcm.FcmRedisMessage;
import b1nd.dodam.firebase.client.FCMClient;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class FcmProcessor {

    private final FCMClient fcmClient;
    private final FCMRedisQueueProducer redisQueueProducer;

    private static final List<Long> RETRY_DELAYS = List.of(3_000L, 30_000L, 60_000L);

    public void process(FcmEvent event) {
        boolean success = fcmClient.sendMessage(event.token(), event.title(), event.body());
        FcmRedisMessage message = FcmMessageConverter.toRedisMessage(
                event.nextRetry(),
                getDelayForRetry(event.retryCount())
        );
        if (!success && event.retryCount() < event.maxRetry()) {
            redisQueueProducer.enqueueToRetryQueue(message);
        } else if (event.retryCount() >= event.maxRetry()) {
            redisQueueProducer.enqueueToFailedQueue(message);
        }
    }

    public long getDelayForRetry(int retryCount) {
        if (retryCount < RETRY_DELAYS.size()) {
            return RETRY_DELAYS.get(retryCount);
        }
        return RETRY_DELAYS.get(RETRY_DELAYS.size() - 1);
    }
}
