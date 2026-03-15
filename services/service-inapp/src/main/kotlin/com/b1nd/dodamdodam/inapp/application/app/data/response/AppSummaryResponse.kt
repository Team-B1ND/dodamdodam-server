package com.b1nd.dodamdodam.inapp.application.app.data.response

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppStatusType
import java.util.UUID

data class AppSummaryResponse(
    val appId: UUID,
    val teamId: UUID,
    val name: String,
    val subtitle: String,
    val description: String?,
    val iconUrl: String,
    val darkIconUrl: String?,
    val inquiryMail: String,
    val releaseEnabled: Boolean?,
    val releaseStatus: AppStatusType?,
)
