package com.b1nd.dodamdodam.nightstudy.application.data.request

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import java.time.LocalDate
import java.util.UUID

data class ApplyNightStudyProjectRequest(
    val type: NightStudyType,
    val name: String,
    val description: String,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val memberUserIds: List<UUID>
)
