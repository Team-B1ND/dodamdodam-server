package b1nd.dodaminfra.redis;

import io.lettuce.core.ReadFrom;
import lombok.RequiredArgsConstructor;
import org.springframework.cache.annotation.EnableCaching;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.cache.RedisCacheConfiguration;
import org.springframework.data.redis.cache.RedisCacheManager;
import org.springframework.data.redis.connection.RedisConnectionFactory;
import org.springframework.data.redis.connection.RedisStaticMasterReplicaConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceClientConfiguration;
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@EnableCaching
@Configuration
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties redisProperties;
    private final int DEFAULT_EXPIRE_SECONDS = 86400; // 1 day
    private final int DAY_EXPIRE_SECONDS = 604800; // 1 week
    private final int MONTH_EXPIRE_SECONDS = 86400 * 31; // 1 month

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(
                redisProperties.getMaster().getHost(),
                redisProperties.getMaster().getPort()
        );
        redisProperties.getSlaves().forEach(slave -> staticMasterReplicaConfiguration.addNode(slave.getHost(), slave.getPort()));
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

    @Bean
    public RedisCacheManager redisCacheManager() {

        RedisCacheConfiguration redisCacheConfiguration = RedisCacheConfiguration.defaultCacheConfig()
                .serializeKeysWith(RedisSerializationContext.SerializationPair.fromSerializer(new StringRedisSerializer()))
                .serializeValuesWith(RedisSerializationContext.SerializationPair.fromSerializer(new GenericJackson2JsonRedisSerializer()))
                .entryTtl(Duration.ofSeconds(DEFAULT_EXPIRE_SECONDS));

        Map<String, RedisCacheConfiguration> cacheConfigurations = new HashMap<>();

        // day
        cacheConfigurations.put("meal-day", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(DAY_EXPIRE_SECONDS)));
        // month
        cacheConfigurations.put("meal-month", RedisCacheConfiguration.defaultCacheConfig()
                .entryTtl(Duration.ofSeconds(MONTH_EXPIRE_SECONDS)));

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }
}
