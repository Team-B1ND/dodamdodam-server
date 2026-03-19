package com.b1nd.dodamdodam.neis.infrastructure.scheduler

import com.b1nd.dodamdodam.neis.domain.schedule.service.ScheduleService
import com.b1nd.dodamdodam.neis.infrastructure.comcigan.ComciganClient
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component
import org.springframework.transaction.annotation.Transactional
import java.time.DayOfWeek
import java.time.LocalDate

@Component
class ScheduleScheduler(
    private val comciganClient: ComciganClient,
    private val scheduleService: ScheduleService,
) {
    private val log = LoggerFactory.getLogger(javaClass)

    @Scheduled(cron = "0 0 18 * * FRI")
    @Transactional
    fun fetchNextWeekSchedules() {
        val nextMonday = LocalDate.now().with(DayOfWeek.MONDAY).plusWeeks(1)
        syncWeekly(nextMonday)
    }

    @Scheduled(cron = "0 40 8 * * MON-FRI")
    @Transactional
    fun fetchTodaySchedules() {
        syncDaily(LocalDate.now())
    }

    private fun syncWeekly(mondayDate: LocalDate) {
        try {
            val schedules = comciganClient.fetchWeeklySchedules(mondayDate)
            schedules.forEach {
                scheduleService.saveOrUpdate(it.date, it.grade, it.room, it.period, it.subject, it.teacher)
            }
        } catch (e: Exception) {
            log.error("다음 주 시간표 동기화 실패: {}", mondayDate, e)
        }
    }

    private fun syncDaily(date: LocalDate) {
        try {
            val schedules = comciganClient.fetchDailySchedules(date)
            schedules.forEach {
                scheduleService.saveOrUpdate(it.date, it.grade, it.room, it.period, it.subject, it.teacher)
            }
        } catch (e: Exception) {
            log.error("당일 시간표 동기화 실패: {}", date, e)
        }
    }
}