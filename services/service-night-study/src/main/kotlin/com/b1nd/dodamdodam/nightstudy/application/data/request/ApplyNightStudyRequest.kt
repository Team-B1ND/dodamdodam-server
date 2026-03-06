package com.b1nd.dodamdodam.nightstudy.application.data.request

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.NightStudyType
import java.time.LocalDate

data class ApplyNightStudyRequest(
    val content: String,
    val type: NightStudyType = NightStudyType.NIGHT_STUDY_2,
    val doNeedPhone: Boolean = false,
    val reasonForPhone: String? = null,
    val startAt: LocalDate,
    val endAt: LocalDate
)
