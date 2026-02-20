package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDate
import java.util.UUID

interface OutSleepingRepository : JpaRepository<OutSleepingEntity, Long> {

    // 날짜별 조회: startAt <= date <= endAt 인 외박 전체
    fun findByStartAtLessThanEqualAndEndAtGreaterThanEqual(
        endDate: LocalDate,
        startDate: LocalDate
    ): List<OutSleepingEntity>

    // 내 외박 조회
    fun findByStudentId(studentId: UUID): List<OutSleepingEntity>

    // 유효한 외박 조회: ALLOWED + 오늘이 startAt~endAt 범위 안
    fun findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
        status: OutSleepingStatus,
        endDate: LocalDate,
        startDate: LocalDate
    ): List<OutSleepingEntity>

    // 중복 신청 여부 확인: PENDING/ALLOWED 상태에서 날짜가 겹치는 경우
    @Query("""
        SELECT COUNT(o) > 0
        FROM OutSleepingEntity o
        WHERE o.studentId = :studentId
          AND o.status IN ('PENDING', 'ALLOWED')
          AND o.startAt <= :endAt
          AND o.endAt >= :startAt
    """)
    fun existsOverlapping(
        @Param("studentId") studentId: UUID,
        @Param("startAt") startAt: LocalDate,
        @Param("endAt") endAt: LocalDate
    ): Boolean
}
