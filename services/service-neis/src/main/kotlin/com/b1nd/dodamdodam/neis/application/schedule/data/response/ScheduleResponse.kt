package com.b1nd.dodamdodam.neis.application.schedule.data.response

import java.time.LocalDate

data class ScheduleResponse(
    val date: LocalDate,
    val grade: Int,
    val room: Int,
    val period: Int,
    val subject: String,
    val teacher: String,
)
