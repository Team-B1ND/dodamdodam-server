package com.b1nd.dodamdodam.outsleeping.application.data.response

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatusType
import java.time.LocalDate
import java.util.UUID

data class OutSleepingResponse(
    val userId: UUID,
    val reason: String,
    val status: OutSleepingStatusType,
    val rejectReason: String?,
    val startAt: LocalDate,
    val endAt: LocalDate
)
