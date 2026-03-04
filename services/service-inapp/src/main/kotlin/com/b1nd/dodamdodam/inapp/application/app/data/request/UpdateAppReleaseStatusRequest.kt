package com.b1nd.dodamdodam.inapp.application.app.data.request

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppReleaseStatusType
import java.util.UUID

data class UpdateAppReleaseStatusRequest(
    val releaseId: UUID,
    val status: AppReleaseStatusType,
    val denyResult: String? = null,
)
