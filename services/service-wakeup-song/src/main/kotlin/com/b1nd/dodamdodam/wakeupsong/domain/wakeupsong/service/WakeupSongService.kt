package com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.service

import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.entity.WakeupSongEntity
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.enumeration.WakeupSongStatusType
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongAlreadyAppliedException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongNotFoundException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.exception.WakeupSongNotApplicantException
import com.b1nd.dodamdodam.wakeupsong.domain.wakeupsong.repository.WakeupSongRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.LocalTime
import java.util.UUID

@Service
class WakeupSongService(
    private val repository: WakeupSongRepository
) {

    fun save(entity: WakeupSongEntity): WakeupSongEntity {
        val todayStart = LocalDateTime.of(LocalDate.now(), LocalTime.MIDNIGHT)
        if (repository.existsByStudentIdAndCreatedAtAfter(entity.studentId, todayStart)) {
            throw WakeupSongAlreadyAppliedException()
        }
        return repository.save(entity)
    }

    fun findById(id: Long): WakeupSongEntity =
        repository.findById(id).orElseThrow { WakeupSongNotFoundException() }

    fun findAllByStatus(status: WakeupSongStatusType): List<WakeupSongEntity> =
        repository.findAllByStatus(status)

    fun findAllByStudentId(studentId: UUID): List<WakeupSongEntity> =
        repository.findAllByStudentId(studentId)

    fun allow(id: Long) {
        val entity = findById(id)
        entity.status = WakeupSongStatusType.ALLOWED
        entity.playAt = LocalDate.now().plusDays(1)
    }

    fun deny(id: Long) {
        val entity = findById(id)
        entity.status = WakeupSongStatusType.DENIED
    }

    fun deleteById(id: Long, studentId: UUID) {
        val entity = findById(id)
        if (entity.studentId != studentId) throw WakeupSongNotApplicantException()
        repository.delete(entity)
    }

    fun deleteByIdAsAdmin(id: Long) {
        val entity = findById(id)
        repository.delete(entity)
    }
}
