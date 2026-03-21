package com.b1nd.dodamdodam.gateway.domain.openapi.repository

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class OpenApiKeyCacheRepository(
    private val redisTemplate: ReactiveStringRedisTemplate
) {
    fun get(appPublicId: String): Mono<String> =
        redisTemplate.opsForValue().get(key(appPublicId))

    fun set(appPublicId: String, apiKeyHash: String, ttl: Duration): Mono<Boolean> =
        redisTemplate.opsForValue().set(key(appPublicId), apiKeyHash, ttl)

    private fun key(appPublicId: String) = "openapi:apikey:$appPublicId"
}