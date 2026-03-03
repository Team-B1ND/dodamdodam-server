package com.b1nd.dodamdodam.inapp.domain.team.repository

import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface TeamRepository: JpaRepository<TeamEntity, Long> {
    fun findByName(name: String): TeamEntity?
    fun existsByName(name: String): Boolean
    fun findByPublicId(publicId: UUID): TeamEntity?
}