package com.b1nd.dodamdodam.nightstudy.application.data.request

import com.b1nd.dodamdodam.nightstudy.domain.project.enumeration.NightStudyProjectType
import java.time.LocalDate
import java.util.UUID

data class ApplyNightStudyProjectRequest(
    val type: NightStudyProjectType,
    val name: String,
    val description: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val memberUserIds: List<UUID>
)
