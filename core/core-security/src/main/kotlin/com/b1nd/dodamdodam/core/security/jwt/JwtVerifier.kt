package com.b1nd.dodamdodam.core.security.jwt

import com.b1nd.dodamdodam.core.security.jwt.data.JwtClaims
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
        val jwt = SignedJWT.parse(token)

        require(jwt.verify(verifier)) { "Invalid JWT signature" }
        val claims = jwt.jwtClaimsSet
        require(jwt.jwtClaimsSet.issuer == issuer) { "Invalid JWT issuer" }
        require(claims.expirationTime.after(Date())) { "Token expired" }
        return JwtClaims(
            userId = UUID.fromString(claims.subject),
            username = claims.getStringClaim("username")
        )
    }

    fun isExpired(token: String): Boolean {
        val jwt = SignedJWT.parse(token)
        return jwt.jwtClaimsSet.expirationTime.before(Date())
    }
}