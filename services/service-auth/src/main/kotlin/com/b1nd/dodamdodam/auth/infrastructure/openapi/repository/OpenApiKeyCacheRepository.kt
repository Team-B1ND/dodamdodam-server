package com.b1nd.dodamdodam.auth.infrastructure.openapi.repository

import org.springframework.data.redis.core.StringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class OpenApiKeyCacheRepository(
    private val redisTemplate: StringRedisTemplate
) {
    fun get(appPublicId: String): String? =
        redisTemplate.opsForValue().get(key(appPublicId))

    fun set(appPublicId: String, apiKey: String, ttl: Duration) {
        redisTemplate.opsForValue().set(key(appPublicId), apiKey, ttl)
    }

    fun delete(appPublicId: String) {
        redisTemplate.delete(key(appPublicId))
    }

    private fun key(appPublicId: String) = "openapi:apikey:$appPublicId"
}