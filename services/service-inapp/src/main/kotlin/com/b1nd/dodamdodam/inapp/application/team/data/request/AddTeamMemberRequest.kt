package com.b1nd.dodamdodam.inapp.application.team.data.request

import java.util.UUID

data class AddTeamMemberRequest(
    val users: List<UUID>,
    val teamId: UUID
)
