package b1nd.dodam.restapi.support.pushalarm;

import b1nd.dodam.domain.redis.fcm.FCMRedisQueueProducer;
import b1nd.dodam.domain.redis.fcm.FcmRedisMessage;
import b1nd.dodam.process.listener.pushalarm.FcmEvent;
import lombok.RequiredArgsConstructor;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.Set;

@Component
@RequiredArgsConstructor
public class PushAlarmRetryScheduler {

    private final FCMRedisQueueProducer redisQueueProducer;
    private final ApplicationEventPublisher publisher;

    @Scheduled(fixedRate = 3000)
    public void processRetryQueue() {
        Set<String> messages = redisQueueProducer.getMessagesFromRetryQueue(System.currentTimeMillis());
        if (messages == null || messages.isEmpty()) return;
        messages.forEach(this::processMessageAsync);
    }

    @Async
    public void processMessageAsync(String msg) {
        FcmRedisMessage fcmRedisMessage = redisQueueProducer.dequeueToRetryQueue(msg);
        publisher.publishEvent(FcmEvent.from(fcmRedisMessage));
    }
}
