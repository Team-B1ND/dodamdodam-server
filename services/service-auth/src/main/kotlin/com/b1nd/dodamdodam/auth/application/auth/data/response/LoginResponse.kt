package com.b1nd.dodamdodam.auth.application.auth.data.response

import com.b1nd.dodamdodam.auth.infrastructure.security.jwt.data.JwtPair

data class LoginResponse(
    val access: String,
    val refresh: String
) {
    companion object {
        fun fromJwtPair(jwtPair: JwtPair) = LoginResponse(jwtPair.accessToken, jwtPair.refreshToken)
    }
}
