package com.b1nd.dodamdodam.inapp.application.team.data

import com.b1nd.dodamdodam.grpc.user.UserResponse
import com.b1nd.dodamdodam.inapp.application.team.data.response.MyTeamResponse
import com.b1nd.dodamdodam.inapp.application.team.data.response.TeamDetailResponse
import com.b1nd.dodamdodam.inapp.application.team.data.response.TeamMemberResponse
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamEntity
import com.b1nd.dodamdodam.inapp.domain.team.entity.TeamMemberEntity
import java.util.UUID

fun TeamEntity.toTeamDetailResponse(isOwner: Boolean) = TeamDetailResponse(
    teamId = publicId!!,
    name = name,
    description = description,
    iconUrl = iconUrl,
    githubUrl = githubUrl,
    isOwner = isOwner,
)

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

fun TeamMemberEntity.toTeamMemberResponse(userResponse: UserResponse) = TeamMemberResponse(
    userId = user,
    name = userResponse.name,
    profileImage = if (userResponse.hasProfileImage()) userResponse.profileImage else null,
    isOwner = isOwner,
)

fun List<TeamMemberEntity>.toTeamMemberResponses(
    userMap: Map<UUID, UserResponse>
): List<TeamMemberResponse> = mapNotNull { member ->
    userMap[member.user]?.let { member.toTeamMemberResponse(it) }
}