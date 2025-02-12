package b1nd.dodam.domain.redis.support.config;

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
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.repository.configuration.EnableRedisRepositories;
import org.springframework.data.redis.serializer.GenericJackson2JsonRedisSerializer;
import org.springframework.data.redis.serializer.RedisSerializationContext;
import org.springframework.data.redis.serializer.StringRedisSerializer;

import java.time.Duration;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableCaching
@EnableRedisRepositories
@RequiredArgsConstructor
public class RedisConfig {

    private final RedisProperties properties;
    private final int DEFAULT_EXPIRE_SECONDS = 86400; // 1 day
    private final int DAY_EXPIRE_SECONDS = 604800; // 1 week
    private final int MONTH_EXPIRE_SECONDS = 86400 * 31; // 1 month

    @Bean
    public RedisConnectionFactory redisConnectionFactory() {
        LettuceClientConfiguration clientConfig = LettuceClientConfiguration.builder()
                .readFrom(ReadFrom.REPLICA_PREFERRED)
                .build();
        RedisStaticMasterReplicaConfiguration staticMasterReplicaConfiguration = new RedisStaticMasterReplicaConfiguration(
                properties.getMaster().host(),
                properties.getMaster().port()
        );
        properties.getSlaves().forEach(slave -> staticMasterReplicaConfiguration.addNode(slave.host(), slave.port()));
        return new LettuceConnectionFactory(staticMasterReplicaConfiguration, clientConfig);
    }

    @Bean
    public RedisConnectionFactory masterRedisConnectionFactory() {
        return new LettuceConnectionFactory(properties.getMaster().host(),
                properties.getMaster().port());
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

        cacheConfigurations.put("members-cache", RedisCacheConfiguration.defaultCacheConfig());

        return RedisCacheManager.RedisCacheManagerBuilder
                .fromConnectionFactory(redisConnectionFactory())
                .cacheDefaults(redisCacheConfiguration)
                .withInitialCacheConfigurations(cacheConfigurations)
                .build();
    }

    @Bean
    public StringRedisTemplate stringRedisTemplate() {
        return new StringRedisTemplate(masterRedisConnectionFactory());
    }

    @Bean
    public RedisTemplate<String, Object> redisTemplate() {
        RedisTemplate<String, Object> redisTemplate = new RedisTemplate<>();
        redisTemplate.setKeySerializer(new StringRedisSerializer());
        redisTemplate.setValueSerializer(new StringRedisSerializer());
        redisTemplate.setConnectionFactory(masterRedisConnectionFactory());

        return redisTemplate;
    }

}
