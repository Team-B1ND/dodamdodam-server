package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.request

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import java.time.LocalDate

data class OutSleepingRequest(
    @field:NotBlank val reason: String,
    @field:NotNull val startAt: LocalDate,
    @field:NotNull val endAt: LocalDate
)
