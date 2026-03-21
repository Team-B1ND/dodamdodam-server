package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request

import java.time.DayOfWeek
import java.time.LocalTime

data class UpdateDeadlineRequest(
    val dayOfWeek: DayOfWeek,
    val time: LocalTime,
)
