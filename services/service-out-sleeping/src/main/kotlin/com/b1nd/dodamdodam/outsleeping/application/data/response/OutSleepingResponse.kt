package com.b1nd.dodamdodam.outsleeping.application.data.response

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class OutSleepingResponse(
    val id: Long,
    val userId: UUID,
    val reason: String,
    val status: OutSleepingStatus,
    val rejectReason: String?,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
