package com.b1nd.dodamdodam.inapp.application.team.data

import com.b1nd.dodamdodam.inapp.application.team.data.response.MyTeamResponse
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamMemberEntity

fun TeamMemberEntity.toMyTeamResponse() = MyTeamResponse(
    teamId = team.publicId!!,
    name = team.name,
    description = team.description,
    iconUrl = team.iconUrl,
    githubUrl = team.githubUrl,
    isOwner = isOwner,
)

fun List<TeamMemberEntity>.toMyTeamResponses(): List<MyTeamResponse> =
    map { it.toMyTeamResponse() }