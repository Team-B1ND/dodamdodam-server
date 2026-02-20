package com.b1nd.dodamdodam.outgoing.application.outgoing.data.request

import java.time.LocalDateTime

data class OutGoingRequest(
    val reason: String,
    val startAt: LocalDateTime,
    val endAt: LocalDateTime
)
