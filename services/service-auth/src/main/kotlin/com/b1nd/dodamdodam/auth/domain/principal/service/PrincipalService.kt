package com.b1nd.dodamdodam.auth.domain.principal.service

import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalEntity
import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalRefreshTokenEntity
import com.b1nd.dodamdodam.auth.domain.principal.exception.UserNotFoundException
import com.b1nd.dodamdodam.auth.domain.principal.repository.PrincipalRefreshTokenRepository
import com.b1nd.dodamdodam.auth.domain.principal.repository.PrincipalRepository
import org.springframework.stereotype.Service

@Service
class PrincipalService(
    private val repository: PrincipalRepository,
    private val refreshTokenRepository: PrincipalRefreshTokenRepository
) {
    fun getByUsername(username: String) =
        repository.findByUsernameAndStatusIsTrue(username)
            ?: throw UserNotFoundException()

    fun saveRefreshToken(principal: PrincipalEntity, refreshToken: String, userAgent: String? = null) {
        refreshTokenRepository.save(PrincipalRefreshTokenEntity(principal, refreshToken, userAgent))
    }
}