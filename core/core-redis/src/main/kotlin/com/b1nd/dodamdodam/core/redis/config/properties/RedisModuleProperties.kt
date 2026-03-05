package com.b1nd.dodamdodam.core.redis.config.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "app.redis")
data class RedisModuleProperties(
    val host: String = "localhost",
    val port: Int = 6379
)
