package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubTimeType
import java.time.LocalDate

data class ClubTimeRequest(
    val type: ClubTimeType,
    val start: LocalDate,
    val end: LocalDate,
)
