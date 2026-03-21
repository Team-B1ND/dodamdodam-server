package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import java.time.DayOfWeek
import java.time.LocalTime

data class DeadlineResponse(
    val startDayOfWeek: DayOfWeek,
    val startTime: LocalTime,
    val endDayOfWeek: DayOfWeek,
    val endTime: LocalTime,
)
