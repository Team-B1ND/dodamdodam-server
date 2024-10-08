package b1nd.dodam.domain.redis.support.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Data
@Configuration
@ConfigurationProperties(prefix = "redis")
public class RedisProperties {

    private RedisInstance master;
    private List<RedisInstance> slaves;

}
