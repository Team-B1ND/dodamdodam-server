package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import java.time.DayOfWeek
import java.time.LocalTime

data class DeadlineResponse(
    val dayOfWeek: DayOfWeek,
    val time: LocalTime,
)
