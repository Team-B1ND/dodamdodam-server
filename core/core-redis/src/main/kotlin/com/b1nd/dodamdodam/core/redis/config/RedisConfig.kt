package com.b1nd.dodamdodam.core.redis.config

import com.b1nd.dodamdodam.core.redis.config.properties.RedisModuleProperties
import com.b1nd.dodamdodam.core.redis.service.RedisService
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.data.redis.connection.RedisStandaloneConfiguration
import org.springframework.data.redis.connection.lettuce.LettuceConnectionFactory
import org.springframework.data.redis.core.StringRedisTemplate

@Configuration
@EnableConfigurationProperties(RedisModuleProperties::class)
class RedisConfig(
    private val properties: RedisModuleProperties
) {
    @Bean
    fun redisConnectionFactory(): LettuceConnectionFactory {
        val config = RedisStandaloneConfiguration(properties.host, properties.port)
        return LettuceConnectionFactory(config)
    }

    @Bean
    fun stringRedisTemplate(
        connectionFactory: LettuceConnectionFactory
    ): StringRedisTemplate = StringRedisTemplate(connectionFactory)

    @Bean
    fun redisService(
        redisTemplate: StringRedisTemplate
    ): RedisService = RedisService(redisTemplate)
}
