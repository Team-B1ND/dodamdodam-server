package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.repository

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatus
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface WakeupSongRepository : JpaRepository<WakeupSongEntity, Long> {

    fun findAllByUserId(userId: UUID): List<WakeupSongEntity>

    fun findAllByStatus(status: WakeupSongStatus): List<WakeupSongEntity>

    fun findAllByStatusAndCreatedAtGreaterThanEqualAndCreatedAtLessThan(
        status: WakeupSongStatus,
        start: LocalDateTime,
        end: LocalDateTime
    ): List<WakeupSongEntity>
}
