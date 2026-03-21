package com.b1nd.dodamdodam.outsleeping.domain.deadline.service

import com.b1nd.dodamdodam.outsleeping.domain.deadline.entity.OutSleepingDeadlineEntity
import com.b1nd.dodamdodam.outsleeping.domain.deadline.repository.OutSleepingDeadlineRepository
import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.exception.OutSleepingDeadlineExceededException
import org.springframework.stereotype.Service
import java.time.DayOfWeek
import java.time.LocalDateTime
import java.time.LocalTime

@Service
class OutSleepingDeadlineService(
    private val deadlineRepository: OutSleepingDeadlineRepository
) {

    fun getOrCreateDefault(): OutSleepingDeadlineEntity =
        deadlineRepository.findAll().firstOrNull()
            ?: deadlineRepository.save(OutSleepingDeadlineEntity())

    fun update(dayOfWeek: DayOfWeek, time: LocalTime): OutSleepingDeadlineEntity {
        val deadline = getOrCreateDefault()
        deadline.dayOfWeek = dayOfWeek
        deadline.time = time
        return deadline
    }

    fun validateDeadline() {
        val deadline = getOrCreateDefault()
        val now = LocalDateTime.now()
        val currentDay = now.dayOfWeek.value
        val deadlineDay = deadline.dayOfWeek.value

        if (currentDay > deadlineDay ||
            (currentDay == deadlineDay && now.toLocalTime().isAfter(deadline.time))
        ) {
            throw OutSleepingDeadlineExceededException()
        }
    }
}
