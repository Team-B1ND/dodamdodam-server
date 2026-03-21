package com.b1nd.dodamdodam.outsleeping.domain.outsleeping.service

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.entity.OutSleepingEntity
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingNotFoundException
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.repository.OutSleepingRepository
import org.springframework.stereotype.Service
import java.time.LocalDate
import java.util.UUID

@Service
class OutSleepingService(
    private val outSleepingRepository: OutSleepingRepository
) {

    fun save(outSleeping: OutSleepingEntity): OutSleepingEntity =
        outSleepingRepository.save(outSleeping)

    fun getById(id: Long): OutSleepingEntity =
        outSleepingRepository.findById(id).orElseThrow { OutSleepingNotFoundException() }

    fun getByUserId(userId: UUID): List<OutSleepingEntity> =
        outSleepingRepository.findAllByUserId(userId)

    fun getByDate(date: LocalDate): List<OutSleepingEntity> =
        outSleepingRepository.findAllByStartAtLessThanEqualAndEndAtGreaterThanEqual(date, date)

    fun getAllowedByDate(date: LocalDate): List<OutSleepingEntity> =
        outSleepingRepository.findAllByStatusAndStartAtLessThanEqualAndEndAtGreaterThanEqual(
            OutSleepingStatus.ALLOWED, date, date
        )

    fun delete(outSleeping: OutSleepingEntity) =
        outSleepingRepository.delete(outSleeping)
}
