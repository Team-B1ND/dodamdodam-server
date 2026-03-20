package com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.request

import java.time.LocalDate
import java.util.UUID

data class ApplyProjectNightStudyRequest(
    val name: String,
    val description: String,
    val period: Int,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val members: List<UUID>,
)
