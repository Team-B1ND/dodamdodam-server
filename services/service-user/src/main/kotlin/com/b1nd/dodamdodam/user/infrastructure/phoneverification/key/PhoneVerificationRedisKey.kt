package com.b1nd.dodamdodam.user.infrastructure.phoneverification.key

import com.b1nd.dodamdodam.core.redis.key.RedisKeyType

enum class PhoneVerificationRedisKey(
    override val prefix: String
): RedisKeyType {
    CODE("phone-verification:code"),
    STATUS("phone-verification:status"),
}
