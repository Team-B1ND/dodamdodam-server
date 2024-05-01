package b1nd.dodaminfra.redis;

import lombok.RequiredArgsConstructor;
import org.springframework.cache.CacheManager;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
@RequiredArgsConstructor
public class RedisUtil {

    private final CacheManager cacheManager;

    public void evictAllCacheValues() {
        Objects.requireNonNull(cacheManager.getCache("meal-day")).clear();
        Objects.requireNonNull(cacheManager.getCache("meal-month")).clear();
    }
}
