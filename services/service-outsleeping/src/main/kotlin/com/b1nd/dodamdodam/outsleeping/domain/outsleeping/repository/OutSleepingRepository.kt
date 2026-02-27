package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate
import java.util.UUID

interface OutSleepingRepository : JpaRepository<OutSleepingEntity, Long>, OutSleepingRepositoryCustom {

    // 날짜별 조회: startAt <= date <= endAt 인 외박 전체
    fun findByStartAtLessThanEqualAndEndAtGreaterThanEqual(
        endDate: LocalDate,
        startDate: LocalDate
    ): List<OutSleepingEntity>

    // 내 외박 조회
    fun findByStudentId(studentId: UUID): List<OutSleepingEntity>

    // 유효한 외박 조회: ALLOWED + 오늘이 startAt~endAt 범위 안
    fun findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
        status: OutSleepingStatusType,
        endDate: LocalDate,
        startDate: LocalDate
    ): List<OutSleepingEntity>
}
