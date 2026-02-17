package com.b1nd.dodamdodam.gateway.domain.passport.repository

import kotlinx.coroutines.reactive.awaitFirstOrNull
import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import java.time.Duration

@Repository
class PassportCacheRepository(
    private val redisTemplate: ReactiveStringRedisTemplate
) {
    suspend fun get(key: String): String? =
        redisTemplate.opsForValue().get(key).awaitFirstOrNull()

    suspend fun set(key: String, value: String, ttl: Duration) =
        redisTemplate.opsForValue().set(key, value, ttl).awaitFirstOrNull()
}