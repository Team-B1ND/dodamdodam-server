package com.b1nd.dodamdodam.nightstudy.application.nightstudy.data.request

import java.time.LocalDate

data class ApplyPersonalNightStudyRequest(
    val description: String,
    val period: Int,
    val startAt: LocalDate,
    val endAt: LocalDate,
    val needPhone: Boolean,
    val needPhoneReason: String?,
)
