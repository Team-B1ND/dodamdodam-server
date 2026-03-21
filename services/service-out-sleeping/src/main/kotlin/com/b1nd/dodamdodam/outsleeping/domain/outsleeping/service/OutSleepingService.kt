package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.service

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingAlreadyProcessedException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotFoundException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotOwnerException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository.OutSleepingRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class OutSleepingService(
    private val outSleepingRepository: OutSleepingRepository
) {

    fun create(outSleeping: OutSleepingEntity): OutSleepingEntity =
        outSleepingRepository.save(outSleeping)

    fun getById(id: Long): OutSleepingEntity =
        outSleepingRepository.findById(id).orElseThrow { OutSleepingNotFoundException() }

    fun getByUserId(userId: UUID): List<OutSleepingEntity> =
        outSleepingRepository.findAllByUserId(userId)

    fun getByDate(date: LocalDate, pageable: Pageable): Page<OutSleepingEntity> =
        outSleepingRepository.findAllByStartAtLessThanEqualAndEndAtGreaterThanEqual(date, date, pageable)

    fun getByStatusAndDate(status: OutSleepingStatus, date: LocalDate): List<OutSleepingEntity> =
        outSleepingRepository.findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            status, date, date
        )

    fun getAllowedByDate(date: LocalDate): List<OutSleepingEntity> =
        getByStatusAndDate(OutSleepingStatus.ALLOWED, date)

    fun validateOwner(outSleeping: OutSleepingEntity, userId: UUID) {
        if (outSleeping.userId != userId) {
            throw OutSleepingNotOwnerException()
        }
    }

    fun validatePending(outSleeping: OutSleepingEntity) {
        if (outSleeping.status != OutSleepingStatus.PENDING) {
            throw OutSleepingAlreadyProcessedException()
        }
    }

    fun delete(outSleeping: OutSleepingEntity) =
        outSleepingRepository.delete(outSleeping)
}
