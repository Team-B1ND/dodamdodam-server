package com.b1nd.dodamdodam.gateway.infrastructure.auth.properties

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.auth")
data class AuthProperties(
    val url: String
)
