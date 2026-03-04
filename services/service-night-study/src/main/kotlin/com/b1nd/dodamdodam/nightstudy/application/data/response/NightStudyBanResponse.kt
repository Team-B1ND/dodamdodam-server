package com.b1nd.dodamdodam.nightstudy.application.data.response

import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class NightStudyBanResponse(
    val id: Long,
    val userId: UUID,
    val reason: String,
    val endedAt: LocalDate,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
