package b1nd.dodam.process.listener.pushalarm;

import b1nd.dodam.domain.redis.fcm.FcmRedisMessage;

public class FcmMessageConverter {

    public static FcmEvent toEvent(FcmRedisMessage message) {
        return new FcmEvent(
            message.token(),
            message.title(),
            message.body(),
            message.retryCount(),
            message.maxRetry()
        );
    }

    public static FcmRedisMessage toRedisMessage(FcmEvent event, long delayMillis) {
        return new FcmRedisMessage(
            event.token(),
            event.title(),
            event.body(),
            event.retryCount(),
            event.maxRetry(),
            System.currentTimeMillis() + delayMillis
        );
    }
}
