package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class CreateAppReleaseRequest(
    val appId: UUID,
    val releaseUrl: String,
    val memo: String? = null,
)
