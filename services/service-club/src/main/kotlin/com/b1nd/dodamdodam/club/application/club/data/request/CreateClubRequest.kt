package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull

data class CreateClubRequest(
    @NotBlank
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val category: String?,
    @NotNull
    val type: ClubType,
)
