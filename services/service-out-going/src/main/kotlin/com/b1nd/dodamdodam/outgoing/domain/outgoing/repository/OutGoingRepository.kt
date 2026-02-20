package com.b1nd.dodamdodam.outgoing.domain.outgoing.repository

import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.OutGoingEntity
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime
import java.util.UUID

interface OutGoingRepository : JpaRepository<OutGoingEntity, Long> {

    // 날짜별 조회: startAt <= dateEnd AND endAt >= dateStart
    fun findByStartAtLessThanEqualAndEndAtGreaterThanEqual(
        dateEnd: LocalDateTime,
        dateStart: LocalDateTime
    ): List<OutGoingEntity>

    // 내 외출 조회
    fun findByStudentId(studentId: UUID): List<OutGoingEntity>

    // 중복 신청 여부 확인: PENDING/ALLOWED 상태에서 시간이 겹치는 경우
    @Query("""
        SELECT COUNT(o) > 0
        FROM OutGoingEntity o
        WHERE o.studentId = :studentId
          AND o.status IN ('PENDING', 'ALLOWED')
          AND o.startAt <= :endAt
          AND o.endAt >= :startAt
    """)
    fun existsOverlapping(
        @Param("studentId") studentId: UUID,
        @Param("startAt") startAt: LocalDateTime,
        @Param("endAt") endAt: LocalDateTime
    ): Boolean
}
