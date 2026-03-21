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

    fun getAll(): List<OutSleepingDeadlineEntity> =
        deadlineRepository.findAll()

    fun update(dayOfWeek: DayOfWeek, time: LocalTime): OutSleepingDeadlineEntity {
        val deadline = deadlineRepository.findByDayOfWeek(dayOfWeek)
        if (deadline != null) {
            deadline.time = time
            return deadline
        }
        return deadlineRepository.save(OutSleepingDeadlineEntity(dayOfWeek, time))
    }

    fun validateDeadline() {
        val deadlines = deadlineRepository.findAll()
        if (deadlines.isEmpty()) return

        val now = LocalDateTime.now()
        val todayDeadline = deadlines.find { it.dayOfWeek == now.dayOfWeek } ?: return

        if (now.toLocalTime().isAfter(todayDeadline.time)) {
            throw OutSleepingDeadlineExceededException()
        }
    }
}
