package com.b1nd.dodamdodam.inapp.application.app.data.request

import com.b1nd.dodamdodam.inapp.domain.app.enumeration.AppServerStatusType
import java.util.UUID

data class UpdateAppServerStatusRequest(
    val appId: UUID,
    val status: AppServerStatusType,
    val denyResult: String? = null,
)
