package com.b1nd.dodamdodam.auth.infrastructure.passport.util

import com.b1nd.dodamdodam.core.security.passport.Passport
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import org.springframework.stereotype.Component
import java.util.UUID

@Component
class PassportSigner {
    fun create(
        userId: UUID?,
        username: String?,
        enabled: Boolean,
        role: List<RoleType>?,
        os: String,
        version: String
    ): Passport {
        return Passport(
            userId,
            username,
            role,
            enabled,
            os,
            version,
            issuedAt = System.currentTimeMillis(),
            expiredAt = System.currentTimeMillis()
        )
    }
}