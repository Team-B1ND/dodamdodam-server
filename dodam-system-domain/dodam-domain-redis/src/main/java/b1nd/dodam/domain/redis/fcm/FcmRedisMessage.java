package b1nd.dodam.domain.redis.fcm;

public record FcmRedisMessage(
        String token,
        String title,
        String body,
        int retryCount,
        int maxRetry,
        long retryAtMillis
) {
    public FcmRedisMessage nextRetry(long delayMillis) {
        return new FcmRedisMessage(
                token, title, body,
                retryCount + 1,
                maxRetry,
                System.currentTimeMillis() + delayMillis
        );
    }
}
