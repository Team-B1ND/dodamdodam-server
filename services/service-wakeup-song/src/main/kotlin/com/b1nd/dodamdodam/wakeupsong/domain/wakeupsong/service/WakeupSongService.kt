package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.service

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatus
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongNotFoundException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.repository.WakeupSongRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Service
class WakeupSongService(
    private val wakeupSongRepository: WakeupSongRepository
) {

    fun save(wakeupSong: WakeupSongEntity): WakeupSongEntity =
        wakeupSongRepository.save(wakeupSong)

    fun getById(id: Long): WakeupSongEntity =
        wakeupSongRepository.findById(id).orElseThrow { WakeupSongNotFoundException() }

    fun getByUserId(userId: UUID): List<WakeupSongEntity> =
        wakeupSongRepository.findAllByUserId(userId)

    fun getAllowed(year: Int, month: Int, day: Int): List<WakeupSongEntity> {
        val date = LocalDate.of(year, month, day)
        val start = LocalDateTime.of(date, LocalTime.MIN)
        val end = LocalDateTime.of(date, LocalTime.MAX)
        return wakeupSongRepository.findAllByStatusAndCreatedAtBetween(WakeupSongStatus.ALLOWED, start, end)
    }

    fun getPending(): List<WakeupSongEntity> =
        wakeupSongRepository.findAllByStatus(WakeupSongStatus.PENDING)

    fun delete(wakeupSong: WakeupSongEntity) =
        wakeupSongRepository.delete(wakeupSong)
}
