package com.b1nd.dodamdodam.auth.application.passport

import com.b1nd.dodamdodam.auth.application.passport.data.response.ExchangePassportResponse
import com.b1nd.dodamdodam.auth.domain.principal.service.PrincipalService
import com.b1nd.dodamdodam.auth.infrastructure.passport.util.PassportSigner
import com.b1nd.dodamdodam.core.common.holder.HeaderRequestHolder
import com.b1nd.dodamdodam.core.security.jwt.JwtVerifier
import com.b1nd.dodamdodam.core.security.jwt.data.JwtClaims
import com.b1nd.dodamdodam.core.security.passport.crypto.PassportCompressor
import jakarta.servlet.http.HttpServletRequest
import org.springframework.http.HttpHeaders
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import ua_parser.Parser
import kotlin.math.log

@Component
@Transactional(rollbackFor = [Exception::class])
class PassportUseCase(
    private val jwtVerifier: JwtVerifier,
    private val service: PrincipalService,
    private val passportSigner: PassportSigner,
    private final val prefix: String = "Bearer "
) {
    fun exchange(): ExchangePassportResponse {
        val request = HeaderRequestHolder.current()
        val claims = extractClaims(request)
        val principal = claims?.let { service.getByUsername(it.username) }
        val (os, version) = extractDeviceInfo(request)

        val passport = passportSigner.create(
            userId = claims?.userId,
            username = claims?.username,
            enabled = principal?.status ?: false,
            role = principal?.roles?.toList(),
            os = os,
            version = version
        )
        return ExchangePassportResponse(PassportCompressor.compress(passport))
    }

    private fun extractClaims(request: HttpServletRequest): JwtClaims? {
        return request.getHeader(HttpHeaders.AUTHORIZATION)
            ?.removePrefix(prefix)
            ?.let { jwtVerifier.verify(it) }
    }

    private fun extractDeviceInfo(request: HttpServletRequest): Pair<String, String> {
        val userAgent = request.getHeader(HttpHeaders.USER_AGENT)
        val client = Parser().parse(userAgent)

        return client.os.family to (client.os.major ?: "unknown")
    }
}