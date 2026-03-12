package com.b1nd.dodamdodam.auth.domain.principal.repository

import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalRefreshTokenEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PrincipalRefreshTokenRepository: JpaRepository<PrincipalRefreshTokenEntity, Long> {
    fun findByToken(token: String): PrincipalRefreshTokenEntity?
    fun deleteByToken(token: String)
}