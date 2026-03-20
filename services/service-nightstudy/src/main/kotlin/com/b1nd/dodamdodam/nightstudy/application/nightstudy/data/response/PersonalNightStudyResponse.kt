package com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.response

import java.time.LocalDate

data class PersonalNightStudyResponse(
    val id: Long,
    val description: String,
    val period: Int,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val needPhone: Boolean,
    val needPhoneReason: String?,
    val rejectionReason: String?,
)
