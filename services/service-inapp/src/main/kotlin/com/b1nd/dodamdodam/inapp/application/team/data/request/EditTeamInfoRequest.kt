package com.b1nd.dodamdodam.inapp.application.team.data.request

import java.util.UUID

data class EditTeamInfoRequest(
    val name: String?,
    val description: String?,
    val iconUrl: String?,
    val githubUrl: String?,
    val teamId: UUID
)
