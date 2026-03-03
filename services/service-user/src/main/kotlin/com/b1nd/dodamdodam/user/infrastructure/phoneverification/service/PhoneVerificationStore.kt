package com.b1nd.dodamdodam.user.infrastructure.phoneverification.service

import com.b1nd.dodamdodam.core.redis.service.RedisService
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.key.PhoneVerificationRedisKey
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.enumeration.PhoneVerificationStatusType
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception.PhoneNotVerifiedException
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception.PhoneVerificationCodeExpiredException
import com.b1nd.dodamdodam.user.infrastructure.phoneverification.exception.PhoneVerificationCodeMismatchException
import com.b1nd.dodamdodam.user.infrastructure.sms.GabiaSmsSender
import org.springframework.stereotype.Service
import java.time.Duration
import java.util.concurrent.ThreadLocalRandom

@Service
class PhoneVerificationStore(
    private val redisService: RedisService,
    private val gabiaSmsSender: GabiaSmsSender
) {
    fun requestCode(phone: String) {
        val code = generateCode()

        redisService.set(PhoneVerificationRedisKey.CODE, phone, code, Duration.ofMinutes(CODE_EXPIRE_MINUTES))
        redisService.set(
            PhoneVerificationRedisKey.STATUS,
            phone,
            PhoneVerificationStatusType.PENDING.name,
            Duration.ofMinutes(STATUS_EXPIRE_MINUTES)
        )
        gabiaSmsSender.send(phone, "[Web발신]\n[인증번호: $code] 도담도담 인증을 1분안에 진행해 주세요.")
    }

    fun verifyCode(phone: String, code: String) {
        val savedCode = redisService.get(PhoneVerificationRedisKey.CODE, phone)
            ?: throw PhoneVerificationCodeExpiredException()
        if (savedCode != code) throw PhoneVerificationCodeMismatchException()

        redisService.delete(PhoneVerificationRedisKey.CODE, phone)
        redisService.set(
            PhoneVerificationRedisKey.STATUS,
            phone,
            PhoneVerificationStatusType.ACTIVE.name,
            Duration.ofMinutes(STATUS_EXPIRE_MINUTES)
        )
    }

    fun ensureActive(phone: String?) {
        if (phone == null || redisService.get(PhoneVerificationRedisKey.STATUS, phone) != PhoneVerificationStatusType.ACTIVE.name) {
            throw PhoneNotVerifiedException()
        }
    }

    private fun generateCode(): String {
        return ThreadLocalRandom.current().nextInt(0, 1_000_000).toString().padStart(6, '0')
    }

    companion object {
        private const val CODE_EXPIRE_MINUTES = 1L
        private const val STATUS_EXPIRE_MINUTES = 2L
    }
}
