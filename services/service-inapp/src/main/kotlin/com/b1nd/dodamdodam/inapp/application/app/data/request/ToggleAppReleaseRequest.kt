package com.b1nd.dodamdodam.inapp.application.app.data.request

import java.util.UUID

data class ToggleAppReleaseRequest(
    val releaseId: UUID,
    val enabled: Boolean,
)
