package com.b1nd.dodamdodam.club.application.club.data.response

import com.b1nd.dodamdodam.club.domain.club.entity.ClubMemberEntity
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPermission
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPriority
import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubStatus

data class ClubMemberResponse(
    val id: Long,
    val clubPermission: ClubPermission,
    val status: ClubStatus,
    val priority: ClubPriority?,
    val introduction: String?,
    val club: ClubDetailResponse?,
) {
    companion object {
        fun fromEntity(clubMember: ClubMemberEntity, teacherName: String? = null): ClubMemberResponse =
            ClubMemberResponse(
                id = clubMember.id!!,
                clubPermission = clubMember.permission,
                status = clubMember.clubStatus,
                priority = clubMember.priority,
                introduction = clubMember.introduction,
                club = ClubDetailResponse.fromEntity(clubMember.club, teacherName),
            )
    }
}
