package com.b1nd.dodamdodam.neis.presentation.schedule

import com.b1nd.dodamdodam.core.security.annotation.authentication.UserAccess
import com.b1nd.dodamdodam.core.security.passport.enumerations.RoleType
import com.b1nd.dodamdodam.neis.application.schedule.ScheduleUseCase
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import java.time.LocalDate

@RestController
@RequestMapping("/schedule")
class ScheduleController(
    private val scheduleUseCase: ScheduleUseCase,
) {
    @UserAccess
    @GetMapping("/me")
    fun getMySchedules() = scheduleUseCase.getMyWeeklySchedules()

    @UserAccess
    @GetMapping
    fun getSchedules(@RequestParam date: LocalDate, @RequestParam grade: Int, @RequestParam room: Int) =
        scheduleUseCase.getSchedulesByDate(date, grade, room)

    @UserAccess
    @GetMapping("/all")
    fun getAllSchedules(@RequestParam date: LocalDate) =
        scheduleUseCase.getAllSchedulesByDate(date)

    @UserAccess(roles = [RoleType.ADMIN])
    @PostMapping("/sync/weekly")
    fun syncWeeklySchedules(@RequestParam mondayDate: LocalDate) =
        scheduleUseCase.syncWeeklySchedules(mondayDate)

    @UserAccess(roles = [RoleType.ADMIN])
    @PostMapping("/sync/daily")
    fun syncDailySchedules(@RequestParam date: LocalDate) =
        scheduleUseCase.syncDailySchedules(date)
}