package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus

data class ClubPassRequest(
    val clubId: Long,
    val studentId: Long,
    val status: ClubStatus,
)
