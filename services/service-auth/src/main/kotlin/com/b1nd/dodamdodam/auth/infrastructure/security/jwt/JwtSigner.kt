package com.b1nd.dodamdodam.auth.infrastructure.security.jwt

import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.data.JwtPair
import com.b1nd.dodamdodam.core.security.jwt.data.JwtClaims
import com.nimbusds.jose.JOSEObjectType
import com.nimbusds.jose.JWSAlgorithm
import com.nimbusds.jose.JWSHeader
import com.nimbusds.jose.crypto.RSASSASigner
import com.nimbusds.jwt.JWTClaimsSet
import com.nimbusds.jwt.SignedJWT
import java.security.interfaces.RSAPrivateKey
import java.time.Instant
import java.util.Date
import java.util.UUID

class JwtSigner(
    privateKey: RSAPrivateKey,
    private val issuer: String,
    private val accessExpireSeconds: Long,
    private val refreshExpireSeconds: Long
) {
    private val signer = RSASSASigner(privateKey)

    fun createTokens(claims: JwtClaims): JwtPair {
        val accessToken = create(
            userId = claims.userId,
            username = claims.username,
            expireSeconds = accessExpireSeconds,
            type = "access"
        )
        val refreshToken = create(
            userId = claims.userId,
            username = "",
            expireSeconds = refreshExpireSeconds,
            type = "refresh"
        )
        return JwtPair(accessToken, refreshToken)
    }

    private fun create(
        userId: UUID,
        username: String?,
        expireSeconds: Long,
        type: String
    ): String {
        val now = Instant.now()
        val expiration = now.plusSeconds(expireSeconds)

        val jwtClaimsBuilder = JWTClaimsSet.Builder()
            .issuer(issuer)
            .issueTime(Date.from(now))
            .expirationTime(Date.from(expiration))
            .subject(userId.toString())
            .claim("type", type)

        username?.let { jwtClaimsBuilder.claim("username", it) }

        val claims = jwtClaimsBuilder.build()
        val header = JWSHeader.Builder(JWSAlgorithm.RS256)
            .type(JOSEObjectType.JWT)
            .build()

        val signedJWT = SignedJWT(header, claims)
        signedJWT.sign(signer)

        return signedJWT.serialize()
    }
}