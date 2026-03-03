package com.b1nd.dodamdodam.club.application.club.data

import com.b1nd.dodamdodam.club.application.club.data.request.CreateClubRequest
import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity

fun CreateClubRequest.toEntity(): ClubEntity {
    return ClubEntity(
        name = name,
        description = description,
        imageUrl = imageUrl,
        category = category,
        type = type,
    )
}
