package com.b1nd.dodamdodam.neis.application.schedule

import com.b1nd.dodamdodam.core.common.data.Response
import com.b1nd.dodamdodam.neis.application.schedule.data.response.ScheduleResponse
import com.b1nd.dodamdodam.neis.application.schedule.data.toResponse
import com.b1nd.dodamdodam.neis.domain.schedule.service.ScheduleService
import com.b1nd.dodamdodam.neis.infrastructure.comcigan.ComciganClient
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.LocalDate

@Component
@Transactional(rollbackFor = [Exception::class])
class ScheduleUseCase(
    private val scheduleService: ScheduleService,
    private val comciganClient: ComciganClient,
) {
    @Transactional(readOnly = true)
    fun getSchedulesByDate(date: LocalDate, grade: Int, room: Int): Response<List<ScheduleResponse>> {
        val schedules = scheduleService.getSchedulesByDateAndRoom(date, grade, room)
        return Response.ok("시간표를 조회했어요.", schedules.map { it.toResponse() })
    }

    @Transactional(readOnly = true)
    fun getAllSchedulesByDate(date: LocalDate): Response<List<ScheduleResponse>> {
        val schedules = scheduleService.getSchedulesByDate(date)
        return Response.ok("시간표를 조회했어요.", schedules.map { it.toResponse() })
    }

    fun syncWeeklySchedules(mondayDate: LocalDate): Response<Any> {
        val schedules = comciganClient.fetchWeeklySchedules(mondayDate)
        schedules.forEach {
            scheduleService.saveOrUpdate(it.date, it.grade, it.room, it.period, it.subject, it.teacher)
        }
        return Response.ok("${mondayDate} 주간 시간표 동기화가 완료되었어요.")
    }

    fun syncDailySchedules(date: LocalDate): Response<Any> {
        val schedules = comciganClient.fetchDailySchedules(date)
        schedules.forEach {
            scheduleService.saveOrUpdate(it.date, it.grade, it.room, it.period, it.subject, it.teacher)
        }
        return Response.ok("${date} 시간표 동기화가 완료되었어요.")
    }
}