package com.b1nd.dodamdodam.nightstudy.application.data.response

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyStatus
import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.NightStudyProjectType
import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.ProjectRoom
import java.time.LocalDate
import java.time.LocalDateTime
import java.util.UUID

data class NightStudyProjectResponse(
    val id: Long,
    val userId: UUID,
    val type: NightStudyProjectType,
    val name: String,
    val description: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val status: NightStudyStatus,
    val room: ProjectRoom?,
    val rejectReason: String?,
    val memberUserIds: List<UUID>,
    val createdAt: LocalDateTime?,
    val modifiedAt: LocalDateTime?
)
