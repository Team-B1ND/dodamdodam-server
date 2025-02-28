package b1nd.dodam.domain.redis.bus.service;

import b1nd.dodam.domain.redis.bus.exception.BusNonceNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.util.Objects;

@RequiredArgsConstructor
@Service
public class BusRedisService {

    private final StringRedisTemplate redisTemplate;
    private final int BUS_NONCE_EXPIRATION = 5;

    public void cacheBusQRNonce(String memberId, String nonce) {
        String key = String.format("bus-nonce:%s", memberId);
        redisTemplate.opsForValue().set(key, nonce, Duration.ofMinutes(BUS_NONCE_EXPIRATION));
    }

    public void validateBusQRNonce(String memberId, String nonce) {
        String key = String.format("bus-nonce:%s", memberId);
        if(Objects.equals(redisTemplate.opsForValue().get(key), nonce)) {
            throw new BusNonceNotFoundException();
        }
    }

    public void evictBusQRNonce(String memberId) {
        String key = String.format("bus-nonce:%s", memberId);
        redisTemplate.delete(key);
    }
}
