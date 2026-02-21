package com.b1nd.dodamdodam.auth.domain.principal.repository

import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface PrincipalRepository : JpaRepository<PrincipalEntity, Long> {
    fun findByUsername(username: String): PrincipalEntity?
    fun findByUserId(userId: UUID): PrincipalEntity?
}
