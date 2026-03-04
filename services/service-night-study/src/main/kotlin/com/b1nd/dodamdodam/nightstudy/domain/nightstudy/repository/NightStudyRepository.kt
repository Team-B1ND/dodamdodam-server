package com.b1nd.dodamdodam.nightstudy.domain.nightstudy.repository

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.entity.NightStudyEntity
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface NightStudyRepository : JpaRepository<NightStudyEntity, Long> {

    fun findAllByUserId(userId: UUID): List<NightStudyEntity>

    fun findAllByStatus(status: NightStudyStatus): List<NightStudyEntity>

    fun findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
        status: NightStudyStatus,
        startAt: LocalDate,
        endAt: LocalDate
    ): List<NightStudyEntity>
}
