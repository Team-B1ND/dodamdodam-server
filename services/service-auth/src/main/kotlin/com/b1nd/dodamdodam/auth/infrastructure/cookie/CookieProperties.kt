package com.b1nd.dodamdodam.auth.infrastructure.cookie

import org.springframework.boot.context.properties.ConfigurationProperties

@ConfigurationProperties("app.cookie")
data class CookieProperties(
    val domain: String = "",
    val secure: Boolean = true,
    val sameSite: String = "Lax",
    val accessTokenName: String = "access_token",
    val refreshTokenName: String = "refresh_token",
    val refreshTokenPath: String = "/auth/refresh"
)
