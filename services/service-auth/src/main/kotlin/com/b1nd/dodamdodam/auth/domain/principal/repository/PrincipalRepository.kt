package com.b1nd.dodamdodam.auth.domain.principal.repository

import com.b1nd.dodamdodam.auth.domain.principal.entity.PrincipalEntity
import org.springframework.data.jpa.repository.JpaRepository

interface PrincipalRepository : JpaRepository<PrincipalEntity, Long> {
    fun findByUsernameAndStatusIsTrue(username: String): PrincipalEntity?
}
