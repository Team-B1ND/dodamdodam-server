package com.b1nd.dodamdodam.nightstudy.application.data.request

import java.time.LocalDate
import java.util.UUID

data class CreateNightStudyBanRequest(
    val userId: UUID,
    val reason: String,
    val endedAt: LocalDate
)
