package com.b1nd.dodamdodam.inapp.application.team.data.request

import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import jakarta.validation.constraints.NotBlank

data class CreateTeamRequest(
    @NotBlank
    val name: String,
    val description: String?,
    val iconUrl: String?,
    val githubUrl: String?,
) {
    fun toTeamEntity(): TeamEntity =
        TeamEntity(
            name = name,
            description = description,
            iconUrl = iconUrl,
            githubUrl = githubUrl
        )
}
