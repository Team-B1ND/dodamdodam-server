package com.b1nd.dodamdodam.club.application.club.data.request

import com.b1nd.dodamdodam.club.domain.club.enumeration.ClubPriority

data class JoinClubMemberRequest(
    val clubId: Long,
    val clubPriority: ClubPriority? = null,
    val introduction: String? = null,
)
