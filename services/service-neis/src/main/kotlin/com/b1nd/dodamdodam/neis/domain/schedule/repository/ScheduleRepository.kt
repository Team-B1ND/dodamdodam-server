package com.b1nd.dodamdodam.neis.domain.schedule.repository

import com.b1nd.dodamdodam.neis.domain.schedule.entity.ScheduleEntity
import org.springframework.data.jpa.repository.JpaRepository
import java.time.LocalDate

interface ScheduleRepository : JpaRepository<ScheduleEntity, Long> {
    fun findAllByDateAndGradeAndRoomOrderByPeriodAsc(date: LocalDate, grade: Int, room: Int): List<ScheduleEntity>

    fun findAllByDateOrderByGradeAscRoomAscPeriodAsc(date: LocalDate): List<ScheduleEntity>

    fun findByDateAndGradeAndRoomAndPeriod(date: LocalDate, grade: Int, room: Int, period: Int): ScheduleEntity?

    fun findAllByDateBetweenAndGradeAndRoomOrderByDateAscPeriodAsc(
        startDate: LocalDate, endDate: LocalDate, grade: Int, room: Int,
    ): List<ScheduleEntity>

    fun deleteAllByDateBetween(startDate: LocalDate, endDate: LocalDate)
}