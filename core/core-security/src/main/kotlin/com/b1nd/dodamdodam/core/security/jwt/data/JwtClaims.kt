package com.b1nd.dodamdodam.core.security.jwt.data

import java.util.UUID

data class JwtClaims(
    val userId: UUID,
    val username: String
)
