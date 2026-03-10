package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface OutSleepingRepository : JpaRepository<OutSleepingEntity, Long> {

    fun findAllByUserId(userId: UUID): List<OutSleepingEntity>

    fun findAllByStartAtLessThanEqualAndEndAtGreaterThanEqual(
        endAt: LocalDate,
        startAt: LocalDate
    ): List<OutSleepingEntity>

    fun findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
        status: OutSleepingStatus,
        endAt: LocalDate,
        startAt: LocalDate
    ): List<OutSleepingEntity>
}
