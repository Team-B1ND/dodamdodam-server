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

    fun get(): OutSleepingDeadlineEntity? =
        deadlineRepository.findAll().firstOrNull()

    fun update(
        startDayOfWeek: DayOfWeek,
        startTime: LocalTime,
        endDayOfWeek: DayOfWeek,
        endTime: LocalTime,
    ): OutSleepingDeadlineEntity {
        val deadline = deadlineRepository.findAll().firstOrNull()
        if (deadline != null) {
            deadline.update(startDayOfWeek, startTime, endDayOfWeek, endTime)
            return deadline
        }
        return deadlineRepository.save(
            OutSleepingDeadlineEntity(startDayOfWeek, startTime, endDayOfWeek, endTime)
        )
    }

    fun validateDeadline() {
        val deadline = deadlineRepository.findAll().firstOrNull() ?: return

        val now = LocalDateTime.now()
        if (!isWithinRange(now.dayOfWeek, now.toLocalTime(), deadline)) {
            throw OutSleepingDeadlineExceededException()
        }
    }

    private fun isWithinRange(
        currentDay: DayOfWeek,
        currentTime: LocalTime,
        deadline: OutSleepingDeadlineEntity,
    ): Boolean {
        val currentDayValue = currentDay.value
        val startDayValue = deadline.startDayOfWeek.value
        val endDayValue = deadline.endDayOfWeek.value

        return if (startDayValue <= endDayValue) {
            // 같은 주 내 범위 (예: 월~금)
            when {
                currentDayValue < startDayValue || currentDayValue > endDayValue -> false
                currentDayValue == startDayValue -> !currentTime.isBefore(deadline.startTime)
                currentDayValue == endDayValue -> !currentTime.isAfter(deadline.endTime)
                else -> true
            }
        } else {
            // 주를 넘기는 범위 (예: 금~일)
            when {
                currentDayValue > endDayValue && currentDayValue < startDayValue -> false
                currentDayValue == startDayValue -> !currentTime.isBefore(deadline.startTime)
                currentDayValue == endDayValue -> !currentTime.isAfter(deadline.endTime)
                else -> true
            }
        }
    }
}
