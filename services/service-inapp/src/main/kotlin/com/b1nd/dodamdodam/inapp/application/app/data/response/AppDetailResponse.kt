package com.b1nd.dodamdodam.inapp.application.app.data.response

import java.util.UUID

data class AppDetailResponse(
    val appId: UUID,
    val teamId: UUID,
    val name: String,
    val subtitle: String,
    val description: String?,
    val iconUrl: String,
    val darkIconUrl: String?,
    val inquiryMail: String,
    val server: AppServerResponse?,
    val active: Boolean,
    val releases: List<AppReleaseResponse>,
)
