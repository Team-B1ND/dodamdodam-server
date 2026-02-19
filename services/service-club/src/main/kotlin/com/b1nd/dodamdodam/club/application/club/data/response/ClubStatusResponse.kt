package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubType

data class ClubStatusResponse(
    val id: Long,
    val name: String,
    val type: ClubType,
    val myStatus: ClubStatus,
) {
    companion object {
        fun fromEntity(clubMember: ClubMemberEntity): ClubStatusResponse =
            ClubStatusResponse(
                id = clubMember.club.id!!,
                name = clubMember.club.name,
                type = clubMember.club.type,
                myStatus = clubMember.clubStatus,
            )
    }
}
