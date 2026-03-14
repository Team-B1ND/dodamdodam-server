package com.b1nd.dodamdodam.inapp.application.team.data.response

import java.util.UUID

data class TeamMemberResponse(
    val userId: UUID,
    val name: String,
    val profileImage: String?,
    val isOwner: Boolean,
)
