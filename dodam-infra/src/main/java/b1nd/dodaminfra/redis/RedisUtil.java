package b1nd.dodaminfra.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    public void flushAllMealData() {
        flushMealOfDay();
        flushMealOfMonth();
        flushCalorieOfDay();
        flushCalorieOfMonth();
    }

    @CacheEvict(value = "meal-of-day", allEntries = true, cacheManager = "redisCacheManager")
    public void flushMealOfDay() {}

    @CacheEvict(value = "meal-of-month", allEntries = true, cacheManager = "redisCacheManager")
    public void flushMealOfMonth() {}

    @CacheEvict(value = "calorie-of-day", allEntries = true, cacheManager = "redisCacheManager")
    public void flushCalorieOfDay() {}

    @CacheEvict(value = "calorie-of-month", allEntries = true, cacheManager = "redisCacheManager")
    public void flushCalorieOfMonth() {}
}
