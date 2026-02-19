package com.b1nd.dodamdodam.gateway.domain.passport.repository

import org.springframework.data.redis.core.ReactiveStringRedisTemplate
import org.springframework.stereotype.Repository
import reactor.core.publisher.Mono
import java.time.Duration

@Repository
class PassportCacheRepository(
    private val redisTemplate: ReactiveStringRedisTemplate
) {
    fun get(key: String): Mono<String?> =
        redisTemplate.opsForValue().get(key)

    fun set(key: String, value: String, ttl: Duration): Mono<Boolean> =
        redisTemplate.opsForValue().set(key, value, ttl)
}