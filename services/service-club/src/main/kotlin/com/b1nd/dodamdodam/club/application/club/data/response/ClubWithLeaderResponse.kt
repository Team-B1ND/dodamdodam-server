package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType

data class ClubWithLeaderResponse(
    val id: Long,
    val name: String,
    val shortDescription: String,
    val description: String?,
    val subject: String,
    val image: String,
    val type: ClubType,
    val teacherName: String?,
    val state: ClubStatus,
    val leader: ClubStudentResponse?,
) {
    companion object {
        fun fromEntity(
            club: ClubEntity,
            leader: ClubStudentResponse? = null,
            teacherName: String? = null,
        ): ClubWithLeaderResponse =
            ClubWithLeaderResponse(
                id = club.id!!,
                name = club.name,
                shortDescription = club.shortDescription,
                description = club.description,
                subject = club.subject,
                image = club.image,
                type = club.type,
                teacherName = teacherName,
                state = club.state,
                leader = leader,
            )
    }
}
