package com.b1nd.dodamdodam.nightstudy.domain.ban.repository

import com.b1nd.dodamdodam.nightstudy.domain.ban.entity.NightStudyBanEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface NightStudyBanRepository : JpaRepository<NightStudyBanEntity, Long> {

    fun findAllByUserId(userId: UUID): List<NightStudyBanEntity>

    fun findAllByUserIdAndEndedAtGreaterThanEqual(userId: UUID, date: LocalDate): List<NightStudyBanEntity>

    fun deleteAllByUserId(userId: UUID)
}
