package com.b1nd.dodamdodam.inapp.domain.app.command

import com.b1nd.dodamdodam.inapp.domain.app.entity.AppEntity
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import java.util.UUID

data class CreateAppCommand(
    val teamId: UUID,
    val name: String,
    val subtitle: String,
    val description: String?,
    val iconUrl: String,
    val darkIconUrl: String?,
    val inquiryMail: String,
    val githubReleaseUrl: String,
    val server: CreateServerCommand?,
) {
    fun toEntity(team: TeamEntity) = AppEntity(
        name = name,
        description = description,
        subtitle = subtitle,
        iconUrl = iconUrl,
        darkIconUrl = darkIconUrl,
        inquiryMail = inquiryMail,
        team = team,
    )
}
