package b1nd.dodaminfra.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    public void flushAllMealData() {
        flushMealOfDay();
        flushMealOfMonth();
    }

    @CacheEvict(value = "meal-day", allEntries = true, cacheManager = "redisCacheManager")
    public void flushMealOfDay() {}

    @CacheEvict(value = "meal-month", allEntries = true, cacheManager = "redisCacheManager")
    public void flushMealOfMonth() {}
}
