package com.b1nd.dodamdodam.auth.domain.principal.service

import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalEntity
import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalRefreshTokenEntity
import com.b1nd.dodamdodam.auth.domain.principal.exception.UserNotFoundException
import com.b1nd.dodamdodam.auth.domain.principal.repository.PrincipalRefreshTokenRepository
import com.b1nd.dodamdodam.auth.domain.principal.repository.PrincipalRepository
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import org.springframework.stereotype.Service
import java.util.UUID

@Service
class PrincipalService(
    private val repository: PrincipalRepository,
    private val refreshTokenRepository: PrincipalRefreshTokenRepository
) {
    fun getByUsername(username: String) =
        repository.findByUsername(username)
            ?: throw UserNotFoundException()

    fun saveRefreshToken(principal: PrincipalEntity, refreshToken: String, userAgent: String? = null) {
        refreshTokenRepository.save(PrincipalRefreshTokenEntity(principal, refreshToken, userAgent))
    }

    fun updatePrincipal(userId: UUID, status: Boolean, username: String, roles: Set<RoleType>) {
        val principal = repository.findByUserId(userId)
            ?: PrincipalEntity(
                userId = userId,
                status = status,
                username = username,
                roles = roles
            )

        principal.update(
            status = status,
            username = username,
            roles = roles
        )

        repository.save(principal)
    }
}
