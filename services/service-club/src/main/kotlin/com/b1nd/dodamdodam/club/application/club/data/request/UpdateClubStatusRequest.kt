package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus

data class UpdateClubStatusRequest(
    val clubIds: List<Long>,
    val status: ClubStatus,
    val reason: String? = null,
)
