package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class DenyAppReleaseRequest(
    val releaseId: UUID,
    val denyResult: String? = null,
)
