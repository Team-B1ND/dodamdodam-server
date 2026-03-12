package com.b1nd.dodamdodam.auth.application.auth

import com.b1nd.dodamdodam.auth.application.auth.data.request.LoginRequest
import com.b1nd.dodamdodam.auth.application.auth.data.response.LoginResponse
import com.b1nd.dodamdodam.auth.infrastructure.cookie.TokenCookieProvider
import com.b1nd.dodamdodam.auth.infrastructure.user.client.UserClient
import com.b1nd.dodamdodam.auth.domain.principal.exception.PasswordIncorrectException
import com.b1nd.dodamdodam.auth.domain.principal.service.PrincipalService
import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.JwtSigner
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.exception.auth.InvalidTokenSignatureException
import com.b1nd.dodamdodam.core.common.holder.HeaderRequestHolder
import com.b1nd.dodamdodam.core.security.jwt.JwtVerifier
import com.b1nd.dodamdodam.core.security.jwt.data.JwtClaims
import com.google.common.net.HttpHeaders
import kotlinx.coroutines.runBlocking
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional

@Component
@Transactional(rollbackFor = [Exception::class])
class AuthUseCase(
    private val userClient: UserClient,
    private val principalService: PrincipalService,
    private val jwtSigner: JwtSigner,
    private val jwtVerifier: JwtVerifier,
    private val cookieProvider: TokenCookieProvider
) {
    fun login(request: LoginRequest): Response<LoginResponse> {
        val userAgent = HeaderRequestHolder.current().getHeader(HttpHeaders.USER_AGENT)
        val isValid: Boolean = runBlocking { userClient.verifyPassword(request.username, request.password) }.getOrThrow()
        if (!isValid) throw PasswordIncorrectException()
        val principal = principalService.getByUsername(request.username)
        val tokens = jwtSigner.createTokens(JwtClaims(principal.userId, principal.username))
        principalService.saveRefreshToken(principal, tokens.refreshToken, userAgent)
        cookieProvider.addTokenCookies(tokens.accessToken, tokens.refreshToken)
        return Response.ok("로그인에 성공했어요.", LoginResponse.fromJwtPair(tokens))
    }

    fun refresh(bodyToken: String?, cookieToken: String?): Response<LoginResponse> {
        val refreshToken = bodyToken
            ?.takeIf { it.isNotBlank() }
            ?.removePrefix("Bearer ")
            ?.trim()
            ?: cookieToken
                ?.takeIf { it.isNotBlank() }
                ?.removePrefix("Bearer ")
                ?.trim()
            ?: throw InvalidTokenSignatureException()
        val userAgent = HeaderRequestHolder.current().getHeader(HttpHeaders.USER_AGENT)
        jwtVerifier.verify(refreshToken)
        val storedToken = principalService.findRefreshToken(refreshToken)
        val principal = storedToken.principal
        val newTokens = jwtSigner.createTokens(JwtClaims(principal.userId, principal.username))
        principalService.rotateRefreshToken(refreshToken, principal, newTokens.refreshToken, userAgent)
        cookieProvider.addTokenCookies(newTokens.accessToken, newTokens.refreshToken)
        return Response.ok("토큰 갱신에 성공했어요.", LoginResponse.fromJwtPair(newTokens))
    }
}
