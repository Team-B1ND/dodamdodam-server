package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType

data class UpdateClubRequest(
    val name: String?,
    val description: String?,
    val imageUrl: String?,
    val category: String?,
    val type: ClubType?,
)
