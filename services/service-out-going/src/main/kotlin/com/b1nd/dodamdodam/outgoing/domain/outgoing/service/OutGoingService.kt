package com.b1nd.dodamdodam.outgoing.domain.outgoing.service

import com.b1nd.dodamdodam.outgoing.domain.outgoing.entity.OutGoingEntity
import com.b1nd.dodamdodam.outgoing.domain.outgoing.enumeration.OutGoingStatus
import com.b1nd.dodamdodam.outgoing.domain.outgoing.exception.OutGoingAlreadyExistsException
import com.b1nd.dodamdodam.outgoing.domain.outgoing.exception.OutGoingForbiddenException
import com.b1nd.dodamdodam.outgoing.domain.outgoing.exception.OutGoingNotFoundException
import com.b1nd.dodamdodam.outgoing.domain.outgoing.exception.OutGoingNotPendingException
import com.b1nd.dodamdodam.outgoing.domain.outgoing.repository.OutGoingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class OutGoingService(
    private val repository: OutGoingRepository
) {
    fun create(entity: OutGoingEntity): OutGoingEntity {
        if (repository.existsOverlapping(entity.studentId, entity.startAt, entity.endAt)) {
            throw OutGoingAlreadyExistsException()
        }
        return repository.save(entity)
    }

    fun findById(id: Long): OutGoingEntity =
        repository.findById(id).orElseThrow { OutGoingNotFoundException() }

    fun allow(id: Long) {
        val entity = findById(id)
        entity.status = OutGoingStatus.ALLOWED
    }

    fun reject(id: Long, rejectReason: String?) {
        val entity = findById(id)
        entity.status = OutGoingStatus.REJECTED
        entity.rejectReason = rejectReason
    }

    fun revert(id: Long) {
        val entity = findById(id)
        entity.status = OutGoingStatus.PENDING
        entity.rejectReason = null
    }

    fun delete(id: Long, studentId: UUID) {
        val entity = findById(id)
        if (entity.studentId != studentId) throw OutGoingForbiddenException()
        if (entity.status != OutGoingStatus.PENDING) throw OutGoingNotPendingException()
        repository.delete(entity)
    }

    fun findByDate(date: LocalDate): List<OutGoingEntity> =
        repository.findByStartAtLessThanEqualAndEndAtGreaterThanEqual(
            date.atTime(23, 59, 59),
            date.atStartOfDay()
        )

    fun findByStudentId(studentId: UUID): List<OutGoingEntity> =
        repository.findByStudentId(studentId)
}
