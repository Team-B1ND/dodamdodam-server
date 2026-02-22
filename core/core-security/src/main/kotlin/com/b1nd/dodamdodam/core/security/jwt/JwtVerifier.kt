package com.b1nd.dodamdodam.core.security.jwt

import com.b1nd.dodamdodam.core.security.jwt.data.JwtClaims
import com.b1nd.dodamdodam.core.common.exception.auth.InvalidTokenSignatureException
import com.b1nd.dodamdodam.core.common.exception.auth.TokenExpiredException
import com.nimbusds.jose.crypto.RSASSAVerifier
import com.nimbusds.jwt.SignedJWT
import java.security.interfaces.RSAPublicKey
import java.util.Date
import java.util.UUID

class JwtVerifier(
    private val publicKey: RSAPublicKey,
    private val issuer: String
) {
    private val verifier = RSASSAVerifier(publicKey)

    fun verify(token: String): JwtClaims {
        val jwt = extractJwt(token)

        if (!jwt.verify(verifier)) throw InvalidTokenSignatureException()
        val claims = jwt.jwtClaimsSet
        if (jwt.jwtClaimsSet.issuer != issuer) throw InvalidTokenSignatureException()
        if(claims.expirationTime.before(Date())) throw TokenExpiredException()
        return JwtClaims(
            userId = UUID.fromString(claims.subject),
            username = claims.getStringClaim("username")
        )
    }

    fun isExpired(token: String): Boolean {
        val jwt = extractJwt(token)
        return jwt.jwtClaimsSet.expirationTime.before(Date())
    }

    private fun extractJwt(token: String) = runCatching { SignedJWT.parse(token) }
        .getOrElse { throw InvalidTokenSignatureException() }
}
