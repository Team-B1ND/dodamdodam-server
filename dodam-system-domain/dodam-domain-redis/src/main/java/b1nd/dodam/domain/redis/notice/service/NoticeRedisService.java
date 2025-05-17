package b1nd.dodam.domain.redis.notice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Service
@RequiredArgsConstructor
public class NoticeRedisService {
    private final StringRedisTemplate redisTemplate;

    public void setNotice(String title, String nttSn){
        String cacheKey = "notice:" + nttSn;
        redisTemplate.opsForValue().set(cacheKey, title, Duration.ofDays(1));
    }

    public boolean validateNotice(String cacheKey) {
        return redisTemplate.hasKey(cacheKey);
    }

}
