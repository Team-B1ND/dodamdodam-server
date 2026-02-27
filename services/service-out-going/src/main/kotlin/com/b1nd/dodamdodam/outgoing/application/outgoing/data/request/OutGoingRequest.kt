package com.b1nd.dodamdodam.outgoing.application.outgoing.data.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDateTime

data class OutGoingRequest(
    @field:NotBlank
    val reason: String,
    @field:NotNull
    val startAt: LocalDateTime,
    @field:NotNull
    val endAt: LocalDateTime
)
