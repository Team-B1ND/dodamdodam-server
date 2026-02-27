package com.b1nd.dodamdodam.outgoing.domain.outgoing.repository

import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.OutGoingEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface OutGoingRepository : JpaRepository<OutGoingEntity, Long>, OutGoingRepositoryCustom {

    fun findByStartAtLessThanEqualAndEndAtGreaterThanEqual(
        dateEnd: LocalDateTime,
        dateStart: LocalDateTime
    ): List<OutGoingEntity>

    fun findByStudentId(studentId: UUID): List<OutGoingEntity>
}
