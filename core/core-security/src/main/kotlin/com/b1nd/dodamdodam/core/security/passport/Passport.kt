package com.b1nd.dodamdodam.core.security.passport

import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.fasterxml.jackson.annotation.JsonProperty
import java.util.UUID

data class Passport(
    val userId: UUID? = null,
    val username: String? = null,
    val role: List<RoleType>? = null,
    val enabled: Boolean,
    val os: String,
    val version: String,
    @JsonProperty("iat")
    val issuedAt: Long,
    @JsonProperty("exp")
    val expiredAt: Long
)
