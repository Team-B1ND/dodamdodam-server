package com.b1nd.dodamdodam.core.security.jwt

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties(prefix = "jwt")
data class JwtProperties(
    val secret: String = "default-secret-key-for-development-only-please-change-in-production",
    val accessTokenExpiration: Long = 3600000, // 1 hour
    val refreshTokenExpiration: Long = 604800000 // 7 days
)
