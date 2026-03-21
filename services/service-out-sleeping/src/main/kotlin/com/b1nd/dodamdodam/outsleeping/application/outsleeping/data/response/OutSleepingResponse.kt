package com.b1nd.dodamdodam.outsleeping.application.outsleeping.data.response

import com.b1nd.dodamdodam.outsleeping.domain.outsleeping.enumeration.OutSleepingStatus
import java.time.LocalDate
import java.time.LocalDateTime

data class OutSleepingResponse(
    val id: Long,
    val reason: String,
    val status: OutSleepingStatus,
    val student: StudentResponse?,
    val rejectReason: String?,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?,
)
