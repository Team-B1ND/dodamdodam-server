package b1nd.dodam.process.listener.pushalarm;

import b1nd.dodam.domain.redis.fcm.FcmRedisMessage;

public record FcmEvent(
    String token,
    String title,
    String body,
    int retryCount,
    int maxRetry
) {
    public FcmEvent(String token, String title, String body) {
        this(token, title, body, 0, 3);
    }

    public static FcmEvent from(FcmRedisMessage message) {
        return new FcmEvent(
            message.token(),
            message.title(),
            message.body(),
            message.retryCount(),
            message.maxRetry()
        );
    }

    public FcmEvent nextRetry() {
        return new FcmEvent(token, title, body, retryCount + 1, maxRetry);
    }
}
