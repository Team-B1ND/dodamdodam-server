package b1nd.dodam.domain.redis.support.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
@ConfigurationProperties(prefix = "auth")
public class MemberRedisProperties {

    private MemberRedisInstance code;
    private MemberRedisInstance access;

}
