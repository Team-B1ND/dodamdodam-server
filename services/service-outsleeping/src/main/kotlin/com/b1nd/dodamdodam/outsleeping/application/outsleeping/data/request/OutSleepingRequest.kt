package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request

import java.time.LocalDate

data class OutSleepingRequest(
    val reason: String,
    val startAt: LocalDate,
    val endAt: LocalDate
)
