package com.b1nd.dodamdodam.auth.infrastructure.security.jwt.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.jwt")
data class JwtProperties(
    val privateKey: String,
    val publicKey: String,
    val accessExpireSeconds: Long,
    val refreshExpireSeconds: Long,
    val issuer: String
)
