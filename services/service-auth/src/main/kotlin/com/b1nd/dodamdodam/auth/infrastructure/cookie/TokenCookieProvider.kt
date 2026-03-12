package com.b1nd.dodamdodam.auth.infrastructure.cookie

import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.properties.JwtProperties
import org.springframework.http.HttpHeaders
import org.springframework.http.ResponseCookie
import org.springframework.stereotype.Component
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.context.request.ServletRequestAttributes
import java.time.Duration

@Component
class TokenCookieProvider(
    private val cookieProperties: CookieProperties,
    private val jwtProperties: JwtProperties
) {
    fun addTokenCookies(accessToken: String, refreshToken: String) {
        val response = currentResponse()
        response.addHeader(HttpHeaders.SET_COOKIE, createAccessTokenCookie(accessToken).toString())
        response.addHeader(HttpHeaders.SET_COOKIE, createRefreshTokenCookie(refreshToken).toString())
    }

    fun addExpiredCookies() {
        val response = currentResponse()
        response.addHeader(HttpHeaders.SET_COOKIE, createExpiredCookie(cookieProperties.accessTokenName, "/").toString())
        response.addHeader(HttpHeaders.SET_COOKIE, createExpiredCookie(cookieProperties.refreshTokenName, cookieProperties.refreshTokenPath).toString())
    }

    private fun currentResponse() =
        (RequestContextHolder.getRequestAttributes() as ServletRequestAttributes).response!!

    private fun createAccessTokenCookie(token: String): ResponseCookie =
        buildCookie(cookieProperties.accessTokenName, token)
            .path("/")
            .maxAge(Duration.ofSeconds(jwtProperties.accessExpireSeconds))
            .build()

    private fun createRefreshTokenCookie(token: String): ResponseCookie =
        buildCookie(cookieProperties.refreshTokenName, token)
            .path(cookieProperties.refreshTokenPath)
            .maxAge(Duration.ofSeconds(jwtProperties.refreshExpireSeconds))
            .build()

    private fun createExpiredCookie(name: String, path: String): ResponseCookie =
        buildCookie(name, "")
            .path(path)
            .maxAge(0)
            .build()

    private fun buildCookie(name: String, value: String): ResponseCookie.ResponseCookieBuilder =
        ResponseCookie.from(name, value)
            .httpOnly(true)
            .secure(cookieProperties.secure)
            .sameSite(cookieProperties.sameSite)
            .apply {
                cookieProperties.domain.takeIf { it.isNotBlank() }?.let { domain(it) }
            }
}
