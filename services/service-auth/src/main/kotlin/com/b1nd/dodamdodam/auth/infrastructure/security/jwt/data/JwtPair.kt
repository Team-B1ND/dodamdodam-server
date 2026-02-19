package com.b1nd.dodamdodam.auth.infrastructure.security.jwt.data

data class JwtPair(
    val accessToken: String,
    val refreshToken: String
)
