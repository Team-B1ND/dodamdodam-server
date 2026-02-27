package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.service

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingAlreadyExistsException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingForbiddenException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotFoundException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotPendingException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository.OutSleepingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class OutSleepingService(
    private val repository: OutSleepingRepository
) {
    fun create(entity: OutSleepingEntity): OutSleepingEntity {
        if (repository.existsOverlapping(entity.studentId, entity.startAt, entity.endAt)) {
            throw OutSleepingAlreadyExistsException()
        }
        return repository.save(entity)
    }

    fun findById(id: Long): OutSleepingEntity =
        repository.findById(id).orElseThrow { OutSleepingNotFoundException() }

    fun allow(id: Long) {
        val entity = findById(id)
        entity.status = OutSleepingStatusType.ALLOWED
    }

    fun reject(id: Long, rejectReason: String?) {
        val entity = findById(id)
        entity.status = OutSleepingStatusType.REJECTED
        entity.rejectReason = rejectReason
    }

    fun revert(id: Long) {
        val entity = findById(id)
        entity.status = OutSleepingStatusType.PENDING
        entity.rejectReason = null
    }

    fun delete(id: Long, studentId: UUID) {
        val entity = findById(id)
        if (entity.studentId != studentId) throw OutSleepingForbiddenException()
        if (entity.status != OutSleepingStatusType.PENDING) throw OutSleepingNotPendingException()
        repository.delete(entity)
    }

    fun findByDate(date: LocalDate): List<OutSleepingEntity> =
        repository.findByStartAtLessThanEqualAndEndAtGreaterThanEqual(date, date)

    fun findByStudentId(studentId: UUID): List<OutSleepingEntity> =
        repository.findByStudentId(studentId)

    fun findValid(today: LocalDate): List<OutSleepingEntity> =
        repository.findByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            OutSleepingStatusType.ALLOWED, today, today
        )
}
