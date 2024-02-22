package b1nd.dodaminfra.redis;

import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

@Component
public class RedisUtil {

    public void flushAllMealData() {
        flushAllMealOfDay();
        flushAllMealOfMonth();
        flushAllCalorieOfDay();
        flushAllCalorieOfMonth();
    }

    @CacheEvict(value = "meal-of-day", allEntries = true, cacheManager = "redisCacheManager")
    public void flushAllMealOfDay() {}

    @CacheEvict(value = "meal-of-month", allEntries = true, cacheManager = "redisCacheManager")
    public void flushAllMealOfMonth() {}

    @CacheEvict(value = "calorie-of-day", allEntries = true, cacheManager = "redisCacheManager")
    public void flushAllCalorieOfDay() {}

    @CacheEvict(value = "calorie-of-month", allEntries = true, cacheManager = "redisCacheManager")
    public void flushAllCalorieOfMonth() {}
}
