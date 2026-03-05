package com.b1nd.dodamdodam.core.redis.annotation

import com.b1nd.dodamdodam.core.redis.config.RedisConfig
import org.springframework.context.annotation.Import

@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
@Import(RedisConfig::class)
annotation class EnableDodamRedis
