package com.b1nd.dodamdodam.nightstudy.application.data.response

data class CombinedNightStudyResponse(
    val nightStudies: List<NightStudyResponse>,
    val projects: List<NightStudyProjectResponse>
)
