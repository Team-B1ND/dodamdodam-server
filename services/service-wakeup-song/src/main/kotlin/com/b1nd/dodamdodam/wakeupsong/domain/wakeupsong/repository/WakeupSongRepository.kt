package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.repository

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatusType
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDateTime
import java.util.UUID

interface WakeupSongRepository : JpaRepository<WakeupSongEntity, Long> {

    fun findAllByStatus(status: WakeupSongStatusType): List<WakeupSongEntity>

    fun findAllByStudentId(studentId: UUID): List<WakeupSongEntity>

    fun existsByStudentIdAndCreatedAtAfter(studentId: UUID, createdAt: LocalDateTime): Boolean
}
