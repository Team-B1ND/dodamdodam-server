package com.b1nd.dodamdodam.user.infrastructure.phoneverification.service

import com.b1nd.dodamdodam.core.redis.service.RedisService
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.key.PhoneVerificationRedisKey
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.enumeration.PhoneVerificationStatusType
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception.PhoneNotVerifiedException
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception.PhoneVerificationCodeExpiredException
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception.PhoneVerificationCodeMismatchException
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom

@Service
class PhoneVerificationStore(
    private val redisService: RedisService,
    private val smsSender: SmsSender
) {
    fun requestCode(phone: String) {
        val code = generateCode()
        val ttl = Duration.ofMinutes(2)

        redisService.set(PhoneVerificationRedisKey.CODE, phone, code, ttl)
        redisService.set(PhoneVerificationRedisKey.STATUS, phone, PhoneVerificationStatusType.PENDING.name, ttl)
        smsSender.send(phone, "인증번호 [$code]를 2분 이내에 입력해주세요.")
    }

    fun verifyCode(phone: String, code: String) {
        val savedCode = redisService.get(PhoneVerificationRedisKey.CODE, phone)
            ?: throw PhoneVerificationCodeExpiredException()
        if (savedCode != code) throw PhoneVerificationCodeMismatchException()

        redisService.delete(PhoneVerificationRedisKey.CODE, phone)
        redisService.set(PhoneVerificationRedisKey.STATUS, phone, PhoneVerificationStatusType.ACTIVE.name)
    }

    fun ensureActive(phone: String?) {
        if (phone == null || redisService.get(PhoneVerificationRedisKey.STATUS, phone) != PhoneVerificationStatusType.ACTIVE.name) {
            throw PhoneNotVerifiedException()
        }
    }

    private fun generateCode(): String {
        return ThreadLocalRandom.current().nextInt(0, 1_000_000).toString().padStart(6, '0')
    }
}
