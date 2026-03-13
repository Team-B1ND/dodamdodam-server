package com.b1nd.dodamdodam.nightstudy.application.data.response

import com.b1nd.dodamdodam.nightstudy.domain.nightstudy.enumeration.ProjectRoom

data class ProjectRoomResponse(
    val room: ProjectRoom,
    val projectId: Long,
    val projectName: String
)
