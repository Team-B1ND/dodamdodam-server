package com.b1nd.dodamdodam.club.application.club.data.request

data class UpdateClubInfoRequest(
    val name: String,
    val shortDescription: String,
    val description: String,
    val image: String,
    val subject: String,
)
