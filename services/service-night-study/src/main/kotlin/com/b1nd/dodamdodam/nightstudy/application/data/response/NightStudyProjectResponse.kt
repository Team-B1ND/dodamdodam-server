package com.b1nd.dodamdodam.nightstudy.application.data.response

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.ProjectRoom
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class NightStudyProjectResponse(
    val id: Long,
    val userId: UUID,
    val type: NightStudyType,
    val name: String,
    val description: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val status: NightStudyStatus,
    val room: ProjectRoom?,
    val rejectReason: String?,
    val userIds: List<UUID>,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
