package com.b1nd.dodamdodam.core.redis.service

import com.b1nd.dodamdodam.core.redis.key.RedisKeyType
import org.springframework.data.redis.core.StringRedisTemplate
import java.time.Duration

class RedisService(
    private val redisTemplate: StringRedisTemplate
) {
    fun set(keyType: RedisKeyType, key: String, value: String, ttl: Duration? = null) {
        if (ttl == null) {
            redisTemplate.opsForValue().set(redisKey(keyType, key), value)
            return
        }
        redisTemplate.opsForValue().set(redisKey(keyType, key), value, ttl)
    }

    fun get(keyType: RedisKeyType, key: String): String? =
        redisTemplate.opsForValue().get(redisKey(keyType, key))

    fun delete(keyType: RedisKeyType, key: String) {
        redisTemplate.delete(redisKey(keyType, key))
    }

    private fun redisKey(keyType: RedisKeyType, key: String): String =
        "${keyType.prefix}:$key"
}
