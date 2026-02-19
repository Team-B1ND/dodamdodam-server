package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType

data class CreateClubRequest(
    val name: String,
    val subject: String,
    val shortDescription: String,
    val description: String,
    val image: String,
    val type: ClubType,
    val studentIds: List<Long>,
)
