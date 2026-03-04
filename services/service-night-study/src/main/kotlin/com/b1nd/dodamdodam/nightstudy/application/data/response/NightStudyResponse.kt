package com.b1nd.dodamdodam.nightstudy.application.data.response

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class NightStudyResponse(
    val id: Long,
    val userId: UUID,
    val content: String,
    val type: NightStudyType,
    val doNeedPhone: Boolean,
    val reasonForPhone: String?,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val status: NightStudyStatus,
    val rejectReason: String?,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
