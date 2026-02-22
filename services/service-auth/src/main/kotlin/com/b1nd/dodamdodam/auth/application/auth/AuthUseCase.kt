package com.b1nd.dodamdodam.auth.application.auth

import com.b1nd.dodamdodam.auth.application.auth.data.request.LoginRequest
import com.b1nd.dodamdodam.auth.application.auth.data.response.LoginResponse
import com.b1nd.dodamdodam.auth.infrastructure.user.client.UserClient
import com.b1nd.dodamdodam.auth.domain.principal.exception.PasswordIncorrectException
import com.b1nd.dodamdodam.auth.domain.principal.service.PrincipalService
import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.JwtSigner
import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.core.common.holder.HeaderRequestHolder
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
    private val jwtSigner: JwtSigner
) {
    fun login(request: LoginRequest): Response<LoginResponse> {
        val userAgent = HeaderRequestHolder.current().getHeader(HttpHeaders.USER_AGENT)
        val isValid: Boolean = runBlocking { userClient.verifyPassword(request.username, request.password) }.getOrThrow()
        if (!isValid) throw PasswordIncorrectException()
        val principal = principalService.getByUsername(request.username)
        val tokens = jwtSigner.createTokens(JwtClaims(principal.userId, principal.username))
        principalService.saveRefreshToken(principal, tokens.refreshToken, userAgent)
        return Response.ok("로그인에 성공했어요.", LoginResponse.fromJwtPair(tokens))
    }
}