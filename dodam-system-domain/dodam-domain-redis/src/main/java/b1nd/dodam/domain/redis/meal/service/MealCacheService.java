package b1nd.dodam.domain.redis.meal.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.stereotype.Service;

import java.util.Objects;

@Service
@RequiredArgsConstructor
public class MealCacheService {

    private final RedisCacheManager redisCacheManager;

    public void evictAll() {
        Objects.requireNonNull(redisCacheManager.getCache("meal-day")).clear();
        Objects.requireNonNull(redisCacheManager.getCache("meal-month")).clear();
    }

}
