package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyBannedEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.util.UUID

interface NightStudyBannedRepository: JpaRepository<NightStudyBannedEntity, Long> {
    fun existsByUserId(userId: UUID): Boolean
}