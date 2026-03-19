package com.b1nd.dodamdodam.neis.domain.schedule.service

import com.b1nd.dodamdodam.neis.domain.schedule.entity.ScheduleEntity
import com.b1nd.dodamdodam.neis.domain.schedule.repository.ScheduleRepository
import org.springframework.stereotype.Service
import java.time.LocalDate

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
) {
    fun getSchedulesByDateAndRoom(date: LocalDate, grade: Int, room: Int): List<ScheduleEntity> =
        scheduleRepository.findAllByDateAndGradeAndRoomOrderByPeriodAsc(date, grade, room)

    fun getSchedulesByDate(date: LocalDate): List<ScheduleEntity> =
        scheduleRepository.findAllByDateOrderByGradeAscRoomAscPeriodAsc(date)

    fun saveOrUpdate(date: LocalDate, grade: Int, room: Int, period: Int, subject: String, teacher: String) {
        val entity = scheduleRepository.findByDateAndGradeAndRoomAndPeriod(date, grade, room, period)
            ?.apply { updateSchedule(subject, teacher) }
            ?: ScheduleEntity(date, grade, room, period, subject, teacher)

        scheduleRepository.save(entity)
    }

    fun deleteSchedulesBetween(startDate: LocalDate, endDate: LocalDate) {
        scheduleRepository.deleteAllByDateBetween(startDate, endDate)
    }
}
