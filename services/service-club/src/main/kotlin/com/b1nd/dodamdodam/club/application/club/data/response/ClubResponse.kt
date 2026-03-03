package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType
import java.time.LocalDateTime
import java.util.UUID

data class ClubResponse(
    val publicId: UUID,
    val name: String,
    val description: String?,
    val imageUrl: String?,
    val category: String?,
    val type: ClubType,
    val createdAt: LocalDateTime,
) {
    companion object {
        fun fromEntity(club: ClubEntity) = ClubResponse(
            publicId = club.publicId!!,
            name = club.name,
            description = club.description,
            imageUrl = club.imageUrl,
            category = club.category,
            type = club.type,
            createdAt = club.createdAt!!,
        )
    }
}
