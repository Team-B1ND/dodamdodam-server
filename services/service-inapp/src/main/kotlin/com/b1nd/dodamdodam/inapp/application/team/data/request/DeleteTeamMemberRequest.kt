package com.b1nd.dodamdodam.inapp.application.team.data.request

import java.util.UUID

data class DeleteTeamMemberRequest(
    val teamId: UUID,
    val users: List<UUID>
)
