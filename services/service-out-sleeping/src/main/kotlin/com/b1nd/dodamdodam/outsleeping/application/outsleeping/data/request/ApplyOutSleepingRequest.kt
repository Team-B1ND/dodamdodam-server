package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request

import java.time.LocalDate

data class ApplyOutSleepingRequest(
    val reason: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
)
