package com.b1nd.dodamdodam.inapp.application.team.data.response

import java.util.UUID

data class TeamDetailResponse(
    val teamId: UUID,
    val name: String,
    val description: String?,
    val iconUrl: String?,
    val githubUrl: String?,
    val isOwner: Boolean
)