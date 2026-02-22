package com.b1nd.dodamdodam.gateway.infrastructure.redis.configuration

import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.ReactiveRedisConnectionFactory
import org.springframework.data.redis.core.ReactiveStringRedisTemplate

@Configuration
class RedisConfig {
    @Bean
    fun reactiveStringRedisTemplate(
        factory: ReactiveRedisConnectionFactory
    ): ReactiveStringRedisTemplate {
        return ReactiveStringRedisTemplate(factory)
    }
}