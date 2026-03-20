package com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.response

import java.time.LocalDate

data class ProjectNightStudyResponse(
    val id: Long,
    val name: String,
    val description: String,
    val period: Int,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val rejectionReason: String?,
)
